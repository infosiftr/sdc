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

import org.openecomp.sdc.be.model.tosca.constraints.exception.ConstraintViolationException;

import javax.validation.constraints.NotNull;

public class MaxLengthConstraint extends AbstractStringPropertyConstraint {

    @NotNull
    private Integer maxLength;

    public MaxLengthConstraint(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public MaxLengthConstraint() {
        super();
    }

    @Override
    protected void doValidate(String propertyValue) throws ConstraintViolationException {
        if (propertyValue.length() > maxLength) {
            throw new ConstraintViolationException("The length of the value is greater than [" + maxLength + "]");
        }
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
