/*-
 * ============LICENSE_START=======================================================
 * SDC
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.openecomp.sdc.be.model;

import org.openecomp.sdc.be.datatypes.elements.ComponentInstanceDataDefinition;
import org.openecomp.sdc.be.datatypes.elements.PropertiesOwner;
import org.openecomp.sdc.common.api.ArtifactGroupTypeEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ComponentInstance extends ComponentInstanceDataDefinition implements PropertiesOwner {

    private Map<String, List<CapabilityDefinition>> capabilities;
    private Map<String, List<RequirementDefinition>> requirements;
    private Map<String, ArtifactDefinition> deploymentArtifacts;
    private Map<String, ArtifactDefinition> artifacts;
    private List<GroupInstance> groupInstances;

    public ComponentInstance() {
        super();
    }

    public ComponentInstance(ComponentInstanceDataDefinition r) {
        super(r);
    }

    public Map<String, List<CapabilityDefinition>> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, List<CapabilityDefinition>> capabilities) {
        this.capabilities = capabilities;
    }

    public Map<String, List<RequirementDefinition>> getRequirements() {
        return requirements;
    }

    public void setRequirements(Map<String, List<RequirementDefinition>> requirements) {
        this.requirements = requirements;
    }

    public Map<String, ArtifactDefinition> getDeploymentArtifacts() {
        return deploymentArtifacts;
    }

    public Map<String, ArtifactDefinition> safeGetDeploymentArtifacts() {
        return deploymentArtifacts == null ? Collections.emptyMap() : deploymentArtifacts;
    }

    public Map<String, ArtifactDefinition> safeGetInformationalArtifacts() {
        return artifacts == null ? Collections.emptyMap() : deploymentArtifacts;
    }

    public void setDeploymentArtifacts(Map<String, ArtifactDefinition> deploymentArtifacts) {
        this.deploymentArtifacts = deploymentArtifacts;
    }

    public Map<String, ArtifactDefinition> getArtifacts() {
        return artifacts;
    }

    public Map<String, ArtifactDefinition> safeGetArtifacts() {
        return artifacts == null ? Collections.emptyMap() : artifacts;
    }

    public void setArtifacts(Map<String, ArtifactDefinition> artifacts) {
        this.artifacts = artifacts;
    }

    public List<GroupInstance> getGroupInstances() {
        return groupInstances;
    }

    public void setGroupInstances(List<GroupInstance> groupInstances) {
        this.groupInstances = groupInstances;
    }

    public String getActualComponentUid() {
        return getIsProxy() ? getSourceModelUid() : getComponentUid();
    }

    public boolean isArtifactExists(ArtifactGroupTypeEnum groupType, String artifactLabel) {
        if (ArtifactGroupTypeEnum.DEPLOYMENT == groupType) {
            return safeGetDeploymentArtifacts().get(artifactLabel) != null;
        }
        return safeGetInformationalArtifacts().get(artifactLabel) != null;
    }
}
