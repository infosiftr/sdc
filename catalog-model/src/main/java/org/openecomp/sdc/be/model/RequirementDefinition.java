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

import org.openecomp.sdc.be.datatypes.elements.RequirementDataDefinition;

/**
 * Specifies the requirements that the Node Type exposes.
 */
public class RequirementDefinition extends RequirementDataDefinition {

    public RequirementDefinition(){
        super();
    }

    public RequirementDefinition(RequirementDefinition other) {
        super(other);
    }

    public RequirementDefinition(RequirementDataDefinition requirementDataDefinition) {
        super(requirementDataDefinition);
    }
}
