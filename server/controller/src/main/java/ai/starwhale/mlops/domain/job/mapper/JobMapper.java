package ai.starwhale.mlops.domain.job.mapper;

import ai.starwhale.mlops.domain.job.JobEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JobMapper {

    List<JobEntity> listJobs(@Param("projectId") Long projectId);

    JobEntity findJobById(@Param("jobId") Long jobId);

    int addJob(JobEntity jobEntity);

    List<JobEntity> findJobByStatusIn(@Param("jobStatuses") List<Integer> jobStatuses);

    void updateJobStatus(@Param("jobIds") List<Long> jobIds,@Param("jobStatus") Integer jobStatus);
}