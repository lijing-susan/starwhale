package ai.starwhale.mlops.api;

import ai.starwhale.mlops.api.protocol.Code;
import ai.starwhale.mlops.api.protocol.ResponseMessage;
import ai.starwhale.mlops.api.protocol.job.JobRequest;
import ai.starwhale.mlops.api.protocol.job.JobVO;
import ai.starwhale.mlops.api.protocol.resulting.EvaluationResult;
import ai.starwhale.mlops.api.protocol.task.TaskVO;
import ai.starwhale.mlops.common.IDConvertor;
import ai.starwhale.mlops.common.PageParams;
import ai.starwhale.mlops.domain.job.JobService;
import ai.starwhale.mlops.domain.task.TaskService;
import ai.starwhale.mlops.exception.SWValidationException;
import ai.starwhale.mlops.exception.SWValidationException.ValidSubject;
import ai.starwhale.mlops.exception.api.StarWhaleApiException;
import com.github.pagehelper.PageInfo;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class JobController implements JobApi{

    @Resource
    private JobService jobService;

    @Resource
    private TaskService taskService;

    @Resource
    private IDConvertor idConvertor;

    @Override
    public ResponseEntity<ResponseMessage<PageInfo<JobVO>>> listJobs(String projectId,
        Integer pageNum, Integer pageSize) {

        List<JobVO> jobVOS = jobService.listJobs(projectId, new PageParams(pageNum, pageSize));
        PageInfo<JobVO> pageInfo = new PageInfo<>(jobVOS);
        return ResponseEntity.ok(Code.success.asResponse(pageInfo));
    }

    @Override
    public ResponseEntity<ResponseMessage<JobVO>> findJob(String projectId, String jobId) {
        JobVO job = jobService.findJob(projectId, jobId);
        return ResponseEntity.ok(Code.success.asResponse(job));
    }

    @Override
    public ResponseEntity<ResponseMessage<PageInfo<TaskVO>>> listTasks(String projectId,
        String jobId, Integer pageNum, Integer pageSize) {

        List<TaskVO> taskVOS = taskService.listTasks(jobId, new PageParams(pageNum, pageSize));
        PageInfo<TaskVO> pageInfo = new PageInfo<>(taskVOS);
        return ResponseEntity.ok(Code.success.asResponse(pageInfo));
    }

    @Override
    public ResponseEntity<ResponseMessage<String>> createJob(String projectId,
        JobRequest jobRequest) {
        String id = jobService.createJob(jobRequest, projectId);

        return ResponseEntity.ok(Code.success.asResponse(id));
    }

    @Override
    public ResponseEntity<ResponseMessage<String>> action(String projectId, String jobId,
        String action) {
        Long iJobId = idConvertor.revert(jobId);
        if("cancel".equals(action)) {
            jobService.cancelJob(iJobId);
        } else if ("suspend".equals(action)) {
            jobService.pauseJob(iJobId);
        } else if ("resume".equals(action)) {
            jobService.resumeJob(iJobId);
        } else {
            throw new StarWhaleApiException(new SWValidationException(ValidSubject.JOB)
                .tip(String.format("Unknown action: %s", action)), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(Code.success.asResponse("Success: " + action));
    }

    @Override
    public ResponseEntity<ResponseMessage<EvaluationResult>> getJobResult(String projectId,
        String jobId) {

        EvaluationResult jobResult = jobService.getJobResult(projectId, jobId);
        return ResponseEntity.ok(Code.success.asResponse(jobResult));
    }

}