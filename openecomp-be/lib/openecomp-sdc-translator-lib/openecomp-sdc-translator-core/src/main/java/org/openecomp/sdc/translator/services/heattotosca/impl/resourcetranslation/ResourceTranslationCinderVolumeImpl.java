/*
 * Copyright © 2016-2018 European Support Limited
 *
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
 */

package org.openecomp.sdc.translator.services.heattotosca.impl.resourcetranslation;

import org.onap.sdc.tosca.datatypes.model.NodeTemplate;
import org.openecomp.sdc.heat.datatypes.HeatBoolean;
import org.openecomp.sdc.heat.services.HeatConstants;
import org.openecomp.sdc.tosca.datatypes.ToscaNodeType;
import org.openecomp.sdc.tosca.services.DataModelUtil;
import org.openecomp.sdc.translator.datatypes.heattotosca.to.TranslateTo;
import org.openecomp.sdc.translator.services.heattotosca.HeatToToscaUtil;
import org.openecomp.sdc.translator.services.heattotosca.mapping.TranslatorHeatToToscaPropertyConverter;

import java.util.Map;


public class ResourceTranslationCinderVolumeImpl extends ResourceTranslationBase {

    private static final String VOLUME_SIZE_PROPERTY_NAME = "size";

    @Override
    public void translate(TranslateTo translateTo) {
        NodeTemplate nodeTemplate = new NodeTemplate();
        nodeTemplate.setType(ToscaNodeType.CINDER_VOLUME);
        nodeTemplate.setProperties(TranslatorHeatToToscaPropertyConverter
                .getToscaPropertiesSimpleConversion(translateTo.getServiceTemplate(),
                        translateTo.getResourceId(), translateTo.getResource().getProperties(),
                        nodeTemplate.getProperties(), translateTo.getHeatFileName(),
                        translateTo.getHeatOrchestrationTemplate(), translateTo.getResource().getType(),
                        nodeTemplate, translateTo.getContext()));
        handleSizeProperty(nodeTemplate.getProperties());
        String toscaReadOnlyPropName =
                HeatToToscaUtil.getToscaPropertyName(translateTo, HeatConstants.READ_ONLY_PROPERTY_NAME);
        Object readOnlyPropVal = nodeTemplate.getProperties().get(toscaReadOnlyPropName);
        if (readOnlyPropVal != null && !(readOnlyPropVal instanceof Map)) {
            nodeTemplate.getProperties().put(toscaReadOnlyPropName, HeatBoolean.eval(readOnlyPropVal));
        }
        DataModelUtil.addNodeTemplate(translateTo.getServiceTemplate(), translateTo.getTranslatedId(),
                nodeTemplate);
    }


    private void handleSizeProperty(Map<String, Object> nodeTemplateProperties) {
        Object size = nodeTemplateProperties.get(VOLUME_SIZE_PROPERTY_NAME);
        if (size == null) {
            return;
        }
        if (size instanceof Map) {
            Map<String, Object> propMap = (Map) size;
            Map.Entry<String, Object> entry = propMap.entrySet().iterator().next();
            String val = "(" + entry.getKey() + " : " + entry.getValue() + ") * 1024";
            nodeTemplateProperties.put(VOLUME_SIZE_PROPERTY_NAME, val);
        } else {
            nodeTemplateProperties.put(VOLUME_SIZE_PROPERTY_NAME, size + "*1024");
        }
    }
}
