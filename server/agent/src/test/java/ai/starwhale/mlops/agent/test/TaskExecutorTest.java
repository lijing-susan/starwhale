/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.com.
 */

package ai.starwhale.mlops.agent.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import ai.starwhale.mlops.agent.configuration.AgentProperties;
import ai.starwhale.mlops.agent.configuration.DockerConfiguration;
import ai.starwhale.mlops.agent.configuration.NodeConfiguration;
import ai.starwhale.mlops.agent.configuration.TaskConfiguration;
import ai.starwhale.mlops.agent.container.ContainerClient;
import ai.starwhale.mlops.agent.node.gpu.GPUInfo;
import ai.starwhale.mlops.agent.node.gpu.NvidiaDetect;
import ai.starwhale.mlops.agent.report.ReportHttpClient;
import ai.starwhale.mlops.agent.node.SourcePool;
import ai.starwhale.mlops.agent.taskexecutor.TaskExecutor;
import ai.starwhale.mlops.agent.taskexecutor.TaskSource;
import ai.starwhale.mlops.agent.taskexecutor.TaskSource.TaskAction;
import ai.starwhale.mlops.agent.taskexecutor.TaskSource.TaskAction.Context;
import com.google.common.jimfs.Jimfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ResourceUtils;

@SpringBootTest(
        classes = StarWhaleAgentTestApplication.class)
@ImportAutoConfiguration({DockerConfiguration.class, TaskConfiguration.class, NodeConfiguration.class})
@TestPropertySource(
        properties = {"sw.task.rebuild.enabled=false", "sw.task.scheduler.enabled=false", "sw.node.sourcePool.init.enabled=false"},
        locations = "classpath:application-integrationtest.yaml")
public class TaskExecutorTest {
    @Autowired
    private AgentProperties agentProperties;

    @MockBean
    private ReportHttpClient reportHttpClient;

    @MockBean
    private ContainerClient containerClient;

    @MockBean
    private NvidiaDetect nvidiaDetect;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private TaskSource.TaskPool taskPool;

    @Autowired
    private SourcePool sourcePool;

    @Test
    @DisplayName("Should create a file on a file system")
    void givenUnixSystem_whenCreatingFile_thenCreatedInPath() throws IOException {
        FileSystem fileSystem = Jimfs.newFileSystem();
        String fileName = "newFile.txt";
        // important!
        // Path pathToStore = fileSystem.getPath(agentProperties.getTask().getInfoPath());
        Path pathToStore = fileSystem.getPath("/opt/starwhale/tasks/");
        Files.createDirectories(pathToStore);
        Path filePath = pathToStore.resolve(fileName);

        try {
            Files.createFile(filePath);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        assertTrue(Files.exists(pathToStore.resolve(fileName)));
    }

    void init() throws FileNotFoundException {
        Mockito.when(containerClient.startContainer(any(), any())).thenReturn(Optional.of("0dbb121b-1c5a-3a75-8063-0e1620edefe5"));
        // todo how to deal with file write
        //Mockito.when(Files.writeString(Path.of(anyString()), anyString())).then(Answers.valueOf("test"));
        Mockito.when(nvidiaDetect.detect()).thenReturn(Optional.of(
                List.of(
                        GPUInfo.builder()
                                .id("1dbb121b-1c5a-3a75-8063-0e1620edefe6")
                                .driverInfo("driver:1.450.8, CUDA:10.1")
                                .brand("xxxx T4").name("swtest")
                                .processInfos(
                                        List.of(GPUInfo.ProcessInfo.builder().pid("1").build())
                                )
                                .build(),
                        GPUInfo.builder()
                                .id("2dbb121b-1c5a-3a75-8063-0e1620edefe8")
                                .driverInfo("driver:1.450.8, CUDA:10.1")
                                .brand("xxxx T4")
                                .name("swtest")
                                .build()
                )
        ));
        URL taskPathUrl = ResourceUtils.getURL("classpath:tasks");
        TaskAction.rebuildTasks.apply(taskPathUrl.getPath().substring(1), Context.builder().taskPool(taskPool).build());
        sourcePool.refresh();
        sourcePool.setToReady();
    }

    @Test
    public void toPreparingTest() throws IOException {
        init();

        assertEquals(2, taskPool.preparingTasks.size());
        // do preparing test
        taskExecutor.dealPreparingTasks();

        assertEquals(1, taskPool.preparingTasks.size());
        assertEquals(1, taskPool.runningTasks.size());

        // taskExecutor.monitorRunningTasks();

    }
}
