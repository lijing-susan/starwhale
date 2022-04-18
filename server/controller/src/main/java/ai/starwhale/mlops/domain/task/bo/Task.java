/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.ai.
 */

package ai.starwhale.mlops.domain.task.bo;

import ai.starwhale.mlops.domain.job.Job;
import ai.starwhale.mlops.domain.swds.index.SWDSBlock;
import ai.starwhale.mlops.domain.system.Agent;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tasks are derived from a Job. Tasks are the executing units of a Job.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    /**
     * unique id for the task
     */
    Long id;

    /**
     * random uuid for the task
     */
    String uuid;

    /**
     * status of the task
     */
    StagingTaskStatus status;

    /**
     * storage directory path of results
     */
    String resultPaths;

    /**
     * blocks may come from different SWDS
     */
    private List<SWDSBlock> swdsBlocks;

    /**
     * the job where the task is derived from
     */
    Job job;

    /**
     * the agent where the task is executed
     */
    Agent agent;

    public Task deepCopy(){
        return Task.builder()
            .id(this.id)
            .uuid(this.uuid)
            .status(new StagingTaskStatus(status.getStatus(),status.getStage()))
            .resultPaths(this.resultPaths)
            .swdsBlocks(List.copyOf(this.swdsBlocks))
            .job(this.job.deepCopy())
            .agent(this.agent.copy())
            .build();
    }


    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Task)){
            return false;
        }
        Task tsk = (Task)obj;
        return this.getUuid().equals(tsk.getUuid());
    }
}