package ai.starwhale.mlops.domain.job;

import ai.starwhale.mlops.common.BaseEntity;
import ai.starwhale.mlops.domain.project.ProjectEntity;
import ai.starwhale.mlops.domain.swds.SWDatasetVersionEntity;
import ai.starwhale.mlops.domain.swmp.SWModelPackageVersionEntity;
import ai.starwhale.mlops.domain.user.UserEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobEntity extends BaseEntity {

    private Long id;

    private String jobUuid;

    private String modelName;

    private Long projectId;

    private ProjectEntity project;

    private Long swmpVersionId;

    private SWModelPackageVersionEntity swmpVersion;

    private Long ownerId;

    private UserEntity owner;

    private LocalDateTime createdTime;

    private LocalDateTime finishedTime;

    private Long durationMs;

    private Integer jobStatus;

    private Long baseImageId;

    private BaseImageEntity baseImage;

    private Integer deviceType;

    private Integer deviceAmount;

    private String resultOutputPath;


}