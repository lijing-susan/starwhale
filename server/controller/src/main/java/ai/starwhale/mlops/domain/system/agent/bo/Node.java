/*
 * Copyright 2022 Starwhale, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.starwhale.mlops.domain.system.agent.bo;

import ai.starwhale.mlops.domain.node.Device;
import ai.starwhale.mlops.domain.system.agent.AgentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import lombok.NoArgsConstructor;

/**
 * Node is a machine/ a virtual machine or even a K8S pod in the cluster
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Node {

    /**
     * the version of the agent that is deployed on this node
     */
    String agentVersion;

    /**
     * the unique number to identify this node
     */
    String serialNumber;

    /**
     * the ip address of this node
     */
    String ipAddr;

    /**
     * memory size in GB unit
     */
    Float memorySizeGB;

    /**
     * the device holding information
     */
    List<Device> devices;

    AgentStatus status;

    public boolean equals(Object obj){
        if(!(obj instanceof Node)){
            return false;
        }
        Node node = (Node)obj;
        return this.serialNumber.equals(node.getSerialNumber());
    }

}