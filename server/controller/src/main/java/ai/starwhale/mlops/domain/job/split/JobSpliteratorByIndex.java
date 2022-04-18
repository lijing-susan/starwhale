/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.ai.
 */

package ai.starwhale.mlops.domain.job.split;

import ai.starwhale.mlops.domain.job.Job;
import ai.starwhale.mlops.domain.job.Job.JobStatus;
import ai.starwhale.mlops.domain.job.mapper.JobMapper;
import ai.starwhale.mlops.domain.storage.StoragePathCoordinator;
import ai.starwhale.mlops.domain.swds.SWDataSet;
import ai.starwhale.mlops.domain.swds.index.SWDSBlock;
import ai.starwhale.mlops.domain.swds.index.SWDSBlockSerializer;
import ai.starwhale.mlops.domain.swds.index.SWDSIndex;
import ai.starwhale.mlops.domain.swds.index.SWDSIndexLoader;
import ai.starwhale.mlops.domain.task.TaskEntity;
import ai.starwhale.mlops.domain.task.mapper.TaskMapper;
import ai.starwhale.mlops.domain.task.TaskStatus;
import ai.starwhale.mlops.domain.task.bo.StagingTaskStatus;
import ai.starwhale.mlops.domain.task.bo.Task;
import ai.starwhale.mlops.domain.task.bo.TaskBoConverter;
import ai.starwhale.mlops.exception.SWValidationException;
import ai.starwhale.mlops.exception.SWValidationException.ValidSubject;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * split job by swds index
 */
@Slf4j
@Service
public class JobSpliteratorByIndex implements JobSpliterator {

    private final StoragePathCoordinator storagePathCoordinator;

    private final SWDSIndexLoader swdsIndexLoader;

    private final SWDSBlockSerializer swdsBlockSerializer;

    private final TaskMapper taskMapper;

    private final JobMapper jobMapper;

    private final TaskBoConverter taskBoConverter;

    public JobSpliteratorByIndex(StoragePathCoordinator storagePathCoordinator, SWDSIndexLoader swdsIndexLoader, SWDSBlockSerializer swdsBlockSerializer, TaskMapper taskMapper, JobMapper jobMapper, TaskBoConverter taskBoConverter) {
        this.storagePathCoordinator = storagePathCoordinator;
        this.swdsIndexLoader = swdsIndexLoader;
        this.swdsBlockSerializer = swdsBlockSerializer;
        this.taskMapper = taskMapper;
        this.jobMapper = jobMapper;
        this.taskBoConverter = taskBoConverter;
    }

    /**
     * when task amount exceeds 1000, bach insertion will emit an error
     */
    @Value("${sw.taskSize}")
    Integer amountOfTasks =256;
    /**
     * get all data blocks and split them by a simple random number
     * transactional jobStatus->SPLIT taskStatus->NEW
     */
    @Override
    @Transactional
    public List<Task> split(Job job) {
        final List<SWDataSet> swDataSets = job.getSwDataSets();
        Random r=new Random();
        final Map<Integer,List<SWDSBlock>> swdsBlocks = swDataSets.parallelStream()
            .map(swDataSet -> swdsIndexLoader.load(swDataSet.getIndexPath(),swDataSet.getPath()))
            .map(SWDSIndex::getSwdsBlockList)
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(blk->r.nextInt(amountOfTasks)));//one block on task
        List<TaskEntity> taskList;
        try {
            taskList = buildTaskEntities(job, swdsBlocks);
        } catch (JsonProcessingException e) {
            log.error("error swds index ",e);
            throw new SWValidationException(ValidSubject.SWDS);
        }
        taskMapper.addAll(taskList);
        jobMapper.updateJobStatus(List.of(job.getId()),JobStatus.RUNNING.getValue());
        return taskBoConverter.fromTaskEntity(taskList,job);
    }

    private List<TaskEntity> buildTaskEntities(Job job, Map<Integer, List<SWDSBlock>> swdsBlocks)
        throws JsonProcessingException {
        List<TaskEntity> taskEntities = new LinkedList<>();
        for(Entry<Integer, List<SWDSBlock>> entry:swdsBlocks.entrySet()) {
            final String taskUuid = UUID.randomUUID().toString();
            taskEntities.add(TaskEntity.builder()
                .jobId(job.getId())
                .resultPath(storagePath(job.getUuid(),taskUuid))
                .swdsBlocks(swdsBlockSerializer.toString(entry.getValue()))
                .taskStatus(new StagingTaskStatus(TaskStatus.CREATED).getValue())
                .taskUuid(taskUuid)
                .build());
        }
        return taskEntities;
    }

    private String storagePath(String jobId,String taskId) {
        return storagePathCoordinator.taskResultPath(jobId,taskId);
    }
}