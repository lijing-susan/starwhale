/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.com.
 */

package ai.starwhale.mlops.agent.task.action.normal;

import ai.starwhale.mlops.agent.container.ContainerClient;
import ai.starwhale.mlops.agent.node.SourcePool;
import ai.starwhale.mlops.agent.task.EvaluationTask;
import ai.starwhale.mlops.agent.task.EvaluationTask.Stage;
import ai.starwhale.mlops.agent.task.TaskPool;
import ai.starwhale.mlops.agent.task.action.Context;
import ai.starwhale.mlops.agent.task.action.DoTransition;
import ai.starwhale.mlops.agent.task.persistence.TaskPersistence;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbsBaseTaskTransition implements DoTransition<EvaluationTask, EvaluationTask> {

    @Autowired
    protected TaskPersistence taskPersistence;

    @Autowired
    protected TaskPool taskPool;

    @Autowired
    protected SourcePool sourcePool;

    @Autowired
    protected ContainerClient containerClient;

    @Override
    public void pre(EvaluationTask task, Context context) throws Exception {
        task.setStage(Stage.inProgress);
        taskPersistence.save(task);
    }

    @Override
    public void post(EvaluationTask oldTask, EvaluationTask newTask, Context context) throws Exception {
        newTask.setStage(Stage.completed);
        taskPersistence.save(newTask);
    }

    @Override
    public void fail(EvaluationTask task, Context context, Exception e) {
        log.error("execute task:{}, error:{}", JSONUtil.toJsonStr(task), e.getMessage(), e);
    }
}