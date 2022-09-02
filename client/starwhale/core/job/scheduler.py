import time
import typing as t
import threading
import concurrent.futures
from queue import Queue
from pathlib import Path

from loguru import logger
from typing_extensions import Protocol

from starwhale.api._impl.job import Step, Context
from starwhale.core.job.model import Task, STATUS


class Callback(Protocol):
    def callback(
        self, step: Step, tasks: t.List[Task], res: bool, exec_time: float
    ) -> t.Any:
        ...


class StepCallback:
    def callback(
        self, step: Step, tasks: t.List[Task], res: bool, exec_time: float
    ) -> t.Any:
        if res:
            step.status = STATUS.SUCCESS
            logger.debug(f"step:{step} success, run time:{exec_time}")
        else:
            step.status = STATUS.FAILED
            logger.error(f"step:{step} failed, run time:{exec_time}")
            # TODO: whether break process?


class Executor:
    def __init__(
        self,
        concurrency: int,
        step: Step,
        tasks: t.List[Task],
        callback: Callback,
    ):
        self.concurrency = concurrency
        self.step = step
        self.tasks = tasks
        self.callback = callback

    def final(self, res: bool, exec_time: float) -> None:
        self.callback.callback(
            step=self.step, tasks=self.tasks, res=res, exec_time=exec_time
        )


class Scheduler:
    def __init__(
        self,
        project: str,
        version: str,
        module: str,
        workdir: Path,
        dataset_uris: t.List[str],
        steps: t.List[Step],
        kw: t.Dict[str, t.Any] = {},
    ) -> None:
        self.project = project
        self.steps = steps
        self.dataset_uris = dataset_uris
        self.module = module
        self.workdir = workdir
        self.version = version
        self.kw = kw
        self._lock = threading.Lock()

    def schedule(self) -> None:
        queue: Queue[Executor] = Queue()
        p = Producer(
            queue=queue,
            project=self.project,
            steps=self.steps,
            dataset_uris=self.dataset_uris,
            module=self.module,
            workdir=self.workdir,
            version=self.version,
            kw=self.kw,
        )
        p.start()
        c = Consumer(queue)
        c.start()
        p.join()
        queue.join()

    def schedule_single_task(self, step_name: str, task_index: int) -> None:
        _steps = [step for step in self.steps if step_name == step.step_name]
        if len(_steps) == 0:
            raise RuntimeError(f"step:{step_name} not found")
        _step = _steps[0]
        if task_index >= _step.task_num:
            raise RuntimeError(
                f"task_index:{task_index} out of bounds, total:{_step.task_num}"
            )
        _task = Task(
            context=Context(
                project=self.project,
                # todo:use id or version
                version=self.version,
                step=_step.step_name,
                total=_step.task_num,
                index=task_index,
                dataset_uris=self.dataset_uris,
                workdir=self.workdir,
                **self.kw,
            ),
            status=STATUS.INIT,
            module=self.module,
            workdir=self.workdir,
        )
        start_time = time.time()
        res = _task.execute()
        StepCallback().callback(_step, [_task], res, time.time() - start_time)


class Producer(threading.Thread):
    def __init__(
        self,
        queue: Queue,
        project: str,
        version: str,
        module: str,
        workdir: Path,
        dataset_uris: t.List[str],
        steps: t.List[Step],
        kw: t.Dict[str, t.Any] = {},
    ) -> None:
        super().__init__()
        self.queue = queue
        self.project = project
        self.steps = steps
        self.dataset_uris = dataset_uris
        self.module = module
        self.workdir = workdir
        self.version = version
        self.kw = kw

    def _split_tasks(self, step: Step) -> t.List[Task]:
        _tasks = []
        for index in range(step.task_num):
            _tasks.append(
                Task(
                    context=Context(
                        project=self.project,
                        version=self.version,
                        step=step.step_name,
                        total=step.task_num,
                        index=index,
                        dataset_uris=self.dataset_uris,
                        workdir=self.workdir,
                        kw=self.kw,
                    ),
                    status=STATUS.INIT,
                    module=self.module,
                    workdir=self.workdir,
                )
            )
        return _tasks

    def run(self) -> None:
        while True:
            time.sleep(1)
            _wait_steps = []
            _finished_step_names = []
            _failed = False
            for _step in self.steps:
                if _step.status == STATUS.FAILED:
                    # todo break processing
                    logger.error(f"step:{_step.step_name} failed, now exited.")
                    _failed = True
                    pass
                if _step.status == STATUS.SUCCESS:
                    _finished_step_names.append(_step.step_name)
                if _step.status == STATUS.INIT or not _step.status:
                    _wait_steps.append(_step)
            # judge whether a step's dependency all in finished
            if len(_wait_steps) == 0 or _failed:
                self.queue.put(None)
                break
            for _wait in _wait_steps:
                if all(d in _finished_step_names for d in _wait.needs if d):
                    logger.debug(f"produce a step:{_wait}")
                    _wait.status = STATUS.RUNNING
                    _executor = Executor(
                        _wait.concurrency,
                        _wait,
                        self._split_tasks(step=_wait),
                        StepCallback(),
                    )
                    self.queue.put(_executor)


class Consumer(threading.Thread):
    def __init__(self, queue: Queue):
        super().__init__()
        self.queue = queue

    def run(self) -> None:
        logger.debug("Consumer starting")
        # process items from the queue
        while True:
            # get a step from the queue
            executor = self.queue.get()
            # check for signal that we are done
            if executor is None:
                break
            start_time = time.time()
            with concurrent.futures.ThreadPoolExecutor(
                max_workers=executor.step.concurrency
            ) as pool:
                futures = [pool.submit(task.execute) for task in executor.tasks]
                res = all(
                    future.result()
                    for future in concurrent.futures.as_completed(futures)
                )
            logger.debug(f"execute step:{executor.step.step_name}, res:{res}")

            executor.final(
                res=res,
                exec_time=time.time() - start_time,
            )
            # mark the unit of work as processed
            self.queue.task_done()
        # mark the signal as processed
        self.queue.task_done()
        logger.debug("Consumer finished")