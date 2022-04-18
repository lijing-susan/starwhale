/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.ai.
 */

package ai.starwhale.mlops.domain.swmp;

import ai.starwhale.mlops.common.BaseEntity;
import ai.starwhale.mlops.domain.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SWModelPackageVersionEntity extends BaseEntity {

    private Long id;

    private Long swmpId;

    private Long ownerId;

    private UserEntity owner;

    private String versionName;

    private String versionTag;

    private String versionMeta;

    private String storagePath;

}