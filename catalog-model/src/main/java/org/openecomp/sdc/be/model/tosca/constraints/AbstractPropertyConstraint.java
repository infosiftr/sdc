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

package org.openecomp.sdc.be.model.tosca.constraints;

import org.openecomp.sdc.be.model.PropertyConstraint;
import org.openecomp.sdc.be.model.tosca.ToscaType;
import org.openecomp.sdc.be.model.tosca.constraints.exception.ConstraintViolationException;
import org.openecomp.sdc.be.model.tosca.version.ApplicationVersionException;

public abstract class AbstractPropertyConstraint implements PropertyConstraint {

    @Override
    public void validate(ToscaType toscaType, String propertyTextValue) throws ConstraintViolationException {
        try {
            validate(toscaType.convert(propertyTextValue));
        } catch (IllegalArgumentException | ApplicationVersionException e) {
            throw new ConstraintViolationException(
                    "String value [" + propertyTextValue + "] is not valid for type [" + toscaType + "]", e);
        }
    }
}
