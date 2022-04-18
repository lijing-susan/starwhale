package ai.starwhale.mlops.agent.test;

import ai.starwhale.mlops.agent.configuration.AgentProperties;
import ai.starwhale.mlops.agent.container.ContainerClient;
import ai.starwhale.mlops.agent.node.SourcePool;
import ai.starwhale.mlops.agent.node.gpu.GPUDetect;
import ai.starwhale.mlops.agent.node.gpu.GPUInfo;
import ai.starwhale.mlops.agent.task.EvaluationTask;
import ai.starwhale.mlops.agent.task.TaskPool;
import ai.starwhale.mlops.agent.task.action.DoTransition;
import ai.starwhale.mlops.agent.task.executor.TaskExecutor;
import ai.starwhale.mlops.agent.task.persistence.TaskPersistence;
import ai.starwhale.mlops.domain.node.Device;
import ai.starwhale.mlops.domain.swds.index.SWDSBlock;
import ai.starwhale.mlops.domain.swds.index.SWDSDataLocation;
import ai.starwhale.mlops.domain.swmp.SWModelPackage;
import ai.starwhale.mlops.domain.task.TaskStatus;
import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(
        classes = StarWhaleAgentTestApplication.class)
@TestPropertySource(
        properties = {
                "sw.task.rebuild.enabled=false",
                "sw.task.scheduler.enabled=false",
                "sw.node.sourcePool.init.enabled=false",
                // when test,please set these properties with debug configuration
                /*"sw.storage.s3-config.endpoint=http://10.131.0.1:9000",
                "sw.agent.basePath=C:/\\Users/\\gaoxinxing/\\swtest" //*/
        },
        locations = "classpath:application-integrationtest.yaml"
)
public class TaskActionTest {
    @MockBean
    private GPUDetect nvidiaDetect;
    @MockBean
    private ContainerClient containerClient;

    @Autowired
    private TaskPersistence taskPersistence;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    DoTransition<Void, List<EvaluationTask>> rebuildTasksAction;

    @Autowired
    DoTransition<EvaluationTask, EvaluationTask> finishedOrCanceled2ArchivedAction;

    @Autowired
    private TaskPool taskPool;

    @Autowired
    private SourcePool sourcePool;

    @Autowired
    private AgentProperties agentProperties;

    @Test
    public void testPreparing2Running() throws IOException {
        // clear local dir
        FileUtils.cleanDirectory(new File(agentProperties.getBasePath()));

        Mockito.when(containerClient.startContainer(any()))
                .thenReturn(Optional.of("0dbb121b-1c5a-3a75-8063-0e1620edefe5"));
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
        List<EvaluationTask> tasks = List.of(
                EvaluationTask.builder()
                        .id(1234567890L)
                        .status(TaskStatus.PREPARING)
                        .deviceClass(Device.Clazz.GPU)
                        .deviceAmount(1)
                        .imageId("starwhaleai/starwhale:0.1.0-nightly-2022041203")
                        .swModelPackage(SWModelPackage.builder()
                                .id(1234567L)
                                .name("test-swmp")
                                .version("v1")
                                .path("StarWhale/controller/swmp/mnist/meytmy3dge4gcnrtmftdgyjzoazxg3y")
                                .build())
                        .swdsBlocks(List.of(
                                SWDSBlock.builder().id(123456L).locationInput(
                                        SWDSDataLocation.builder().file("test-data").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label").offset(100).size(100).build()
                                ).build(),
                                SWDSBlock.builder().id(123466L).locationInput(
                                        SWDSDataLocation.builder().file("test-data2").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label2").offset(100).size(100).build()
                                ).build()
                        ))
                        .resultPath("todo")
                        .build()
        );
        if (CollectionUtil.isNotEmpty(tasks)) {
            tasks.forEach(taskPool::fill);
            taskPool.setToReady();
        }
        sourcePool.refresh();
        sourcePool.setToReady();
        // do prepare test
        taskExecutor.dealPreparingTasks();
        // check execute result
        assertEquals(0, taskPool.preparingTasks.size());
        assertEquals(1, taskPool.runningTasks.size());
    }

    @Test
    public void testMonitorTask() throws Exception {
        List<EvaluationTask> tasks = List.of(
                EvaluationTask.builder()
                        .id(1234567890L)
                        .status(TaskStatus.RUNNING) // change to runnning
                        .containerId("test-containerid")
                        .deviceClass(Device.Clazz.GPU)
                        .deviceAmount(1)
                        .imageId("starwhaleai/starwhale:0.1.0-nightly-2022041203")
                        .swModelPackage(SWModelPackage.builder()
                                .id(1234567L)
                                .name("test-swmp")
                                .version("v1")
                                .path("StarWhale/controller/swmp/mnist/meytmy3dge4gcnrtmftdgyjzoazxg3y")
                                .build())
                        .swdsBlocks(List.of(
                                SWDSBlock.builder().id(123456L).locationInput(
                                        SWDSDataLocation.builder().file("test-data").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label").offset(100).size(100).build()
                                ).build(),
                                SWDSBlock.builder().id(123466L).locationInput(
                                        SWDSDataLocation.builder().file("test-data2").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label2").offset(100).size(100).build()
                                ).build()
                        ))
                        .resultPath("todo")
                        .build(),
                EvaluationTask.builder()
                        .id(1234567891L)
                        .status(TaskStatus.RUNNING) // change to runnning
                        .containerId("test-containerid2")
                        .deviceClass(Device.Clazz.GPU)
                        .deviceAmount(1)
                        .imageId("starwhaleai/starwhale:0.1.0-nightly-2022041203")
                        .swModelPackage(SWModelPackage.builder()
                                .id(1234567L)
                                .name("test-swmp")
                                .version("v1")
                                .path("StarWhale/controller/swmp/mnist/meytmy3dge4gcnrtmftdgyjzoazxg3y")
                                .build())
                        .swdsBlocks(List.of(
                                SWDSBlock.builder().id(123456L).locationInput(
                                        SWDSDataLocation.builder().file("test-data").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label").offset(100).size(100).build()
                                ).build(),
                                SWDSBlock.builder().id(123466L).locationInput(
                                        SWDSDataLocation.builder().file("test-data2").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label2").offset(100).size(100).build()
                                ).build()
                        ))
                        .resultPath("todo")
                        .build()
        );
        if (CollectionUtil.isNotEmpty(tasks)) {
            tasks.forEach(taskPool::fill);
            taskPool.setToReady();
        }

        // first to monitor
        taskExecutor.monitorRunningTasks();
        assertEquals(2, taskPool.runningTasks.size());

        // change status to ok
        taskPersistence.updateStatus(1234567890L, TaskPersistence.ExecuteStatus.OK);
        // container has changed status to OK
        taskExecutor.monitorRunningTasks();

        assertEquals(1, taskPool.runningTasks.size());
        assertEquals(1, taskPool.uploadingTasks.size());
    }

    @Test
    public void testUpload() {
        List<EvaluationTask> tasks = List.of(
                EvaluationTask.builder()
                        .id(1234567890L)
                        .status(TaskStatus.UPLOADING) // change to UPLOADING
                        .containerId("test-containerid")
                        .deviceClass(Device.Clazz.GPU)
                        .deviceAmount(1)
                        .imageId("starwhaleai/starwhale:0.1.0-nightly-2022041203")
                        .swModelPackage(SWModelPackage.builder()
                                .id(1234567L)
                                .name("test-swmp")
                                .version("v1")
                                .path("StarWhale/controller/swmp/mnist/meytmy3dge4gcnrtmftdgyjzoazxg3y")
                                .build())
                        .swdsBlocks(List.of(
                                SWDSBlock.builder().id(123456L).locationInput(
                                        SWDSDataLocation.builder().file("test-data").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label").offset(100).size(100).build()
                                ).build(),
                                SWDSBlock.builder().id(123466L).locationInput(
                                        SWDSDataLocation.builder().file("test-data2").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label2").offset(100).size(100).build()
                                ).build()
                        ))
                        .resultPath("todo")
                        .build(),
                EvaluationTask.builder()
                        .id(1234567891L)
                        .status(TaskStatus.UPLOADING) // change to UPLOADING
                        .containerId("test-containerid2")
                        .deviceClass(Device.Clazz.GPU)
                        .deviceAmount(1)
                        .imageId("starwhaleai/starwhale:0.1.0-nightly-2022041203")
                        .swModelPackage(SWModelPackage.builder()
                                .id(1234567L)
                                .name("test-swmp")
                                .version("v1")
                                .path("StarWhale/controller/swmp/mnist/meytmy3dge4gcnrtmftdgyjzoazxg3y")
                                .build())
                        .swdsBlocks(List.of(
                                SWDSBlock.builder().id(123456L).locationInput(
                                        SWDSDataLocation.builder().file("test-data").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label").offset(100).size(100).build()
                                ).build(),
                                SWDSBlock.builder().id(123466L).locationInput(
                                        SWDSDataLocation.builder().file("test-data2").offset(0).size(100).build()
                                ).locationLabel(
                                        SWDSDataLocation.builder().file("test-label2").offset(100).size(100).build()
                                ).build()
                        ))
                        .resultPath("todo")
                        .build()
        );
        if (CollectionUtil.isNotEmpty(tasks)) {
            tasks.forEach(taskPool::fill);
            taskPool.setToReady();
        }

        // first to monitor
        taskExecutor.uploadTaskResults();
        assertEquals(2, taskPool.uploadingTasks.size());

    }


    @Test
    public void testArchived() {
        EvaluationTask task = EvaluationTask.builder()
                .id(1234567890L)
                .build();
        assertFalse(Files.exists(Path.of(taskPersistence.pathOfArchived(task.getId()))));
        finishedOrCanceled2ArchivedAction.apply(task, null);
        assertTrue(Files.exists(Path.of(taskPersistence.pathOfArchived(task.getId()))));
    }
}