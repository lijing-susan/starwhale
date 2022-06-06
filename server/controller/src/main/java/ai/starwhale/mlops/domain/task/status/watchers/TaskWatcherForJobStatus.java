/*
 * Copyright 2022 Starwhale, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.starwhale.mlops.domain.task.status.watchers;

import ai.starwhale.mlops.domain.job.status.JobUpdateHelper;
import ai.starwhale.mlops.domain.job.step.Step;
import ai.starwhale.mlops.domain.job.step.mapper.StepMapper;
import ai.starwhale.mlops.domain.job.step.status.StepStatus;
import ai.starwhale.mlops.domain.job.step.status.StepStatusMachine;
import ai.starwhale.mlops.domain.job.step.trigger.StepTriggerContext;
import ai.starwhale.mlops.domain.task.StepHelper;
import ai.starwhale.mlops.domain.task.bo.Task;
import ai.starwhale.mlops.domain.task.status.TaskStatus;
import ai.starwhale.mlops.domain.task.status.TaskStatusChangeWatcher;
import ai.starwhale.mlops.schedule.SWTaskScheduler;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * task status change for job status change
 */
@Slf4j
@Component
@Order(2)
public class TaskWatcherForJobStatus implements TaskStatusChangeWatcher {

    final StepHelper stepHelper;

    final SWTaskScheduler swTaskScheduler;

    final StepStatusMachine stepStatusMachine;

    final StepMapper stepMapper;

    final StepTriggerContext stepTriggerContext;

    final JobUpdateHelper jobUpdateHelper;

    public TaskWatcherForJobStatus(
        StepHelper stepHelper,
        SWTaskScheduler swTaskScheduler,
        StepStatusMachine stepStatusMachine,
        StepMapper stepMapper,
        StepTriggerContext stepTriggerContext,
        JobUpdateHelper jobUpdateHelper) {
        this.stepHelper = stepHelper;
        this.swTaskScheduler = swTaskScheduler;
        this.stepStatusMachine = stepStatusMachine;
        this.stepMapper = stepMapper;
        this.stepTriggerContext = stepTriggerContext;
        this.jobUpdateHelper = jobUpdateHelper;
    }

    @Override
    public void onTaskStatusChange(Task task, TaskStatus oldStatus) {
        Step step = task.getStep();
        synchronized (step){
            Collection<TaskStatus> taskStatuses = step.getTasks().parallelStream().map(Task::getStatus).collect(
                Collectors.toSet());
            StepStatus stepNewStatus = stepHelper.desiredStepStatus(taskStatuses);
            if(step.getStatus() == stepNewStatus){
                log.debug("step status not changed {} id {}",stepNewStatus,step.getId());
                return;
            }
            if(!stepStatusMachine.couldTransfer(step.getStatus(),stepNewStatus)){
                log.warn("step status change unexpectedly from {} to {} of id {} forbidden",step.getStatus(),stepNewStatus,step.getId());
                return;
            }
            updateStepStatus(step, stepNewStatus);
            jobUpdateHelper.updateJob(step.getJob());
            if(step.getStatus() == StepStatus.SUCCESS){
                stepTriggerContext.triggerNextStep(step);
            }

        }
    }

    private void updateStepStatus(Step step, StepStatus stepNewStatus) {
        log.info("step status change from {} to {} with id {}", step.getStatus(), stepNewStatus,
            step.getId());
        step.setStatus(stepNewStatus);
        stepMapper.updateStatus(List.of(step.getId()), stepNewStatus);
    }

}