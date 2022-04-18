/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.com.
 */

package ai.starwhale.mlops.agent.task.initializer;

import ai.starwhale.mlops.agent.task.EvaluationTask;
import ai.starwhale.mlops.agent.task.action.Context;
import ai.starwhale.mlops.agent.task.action.DoTransition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * execute on every startup
 */
@Slf4j
@Order(0)
@Component
@ConditionalOnProperty(name = "sw.task.rebuild.enabled", havingValue = "true", matchIfMissing = true)
public class TaskPoolInitializer implements CommandLineRunner {
    @Autowired
    private DoTransition<Void, List<EvaluationTask>> rebuildTasksAction;

    @Override
    public void run(String... args) throws Exception {
        rebuildTasksAction.apply(Void.TYPE.cast(null),
            Context.builder().build());
    }
}