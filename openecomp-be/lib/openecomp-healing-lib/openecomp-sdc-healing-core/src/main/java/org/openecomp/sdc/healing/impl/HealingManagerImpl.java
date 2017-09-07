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

package org.openecomp.sdc.healing.impl;

import org.openecomp.core.utilities.file.FileUtils;
import org.openecomp.core.utilities.json.JsonUtil;
import org.openecomp.sdc.common.errors.Messages;
import org.openecomp.sdc.datatypes.error.ErrorLevel;
import org.openecomp.sdc.healing.api.HealingManager;
import org.openecomp.sdc.healing.interfaces.Healer;
import org.openecomp.sdc.healing.types.HealCode;
import org.openecomp.sdc.logging.context.impl.MdcDataErrorMessage;
import org.openecomp.sdc.logging.types.LoggerConstants;
import org.openecomp.sdc.logging.types.LoggerErrorCode;
import org.openecomp.sdc.logging.types.LoggerErrorDescription;
import org.openecomp.sdc.logging.types.LoggerTragetServiceName;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Created by Talio on 11/29/2016.
 */
public class HealingManagerImpl implements HealingManager {
  private static String HEALING_CONF_FILE = "healingConfiguration.json";
  private static Map<String, String> healerCodeToImplClass = initHealers();

  @Override
  public Object heal(HealCode code, Map<String, Object> healParameters) {
    String implClassName = healerCodeToImplClass.get(code.name());
    try {
      Healer healerImpl = getHealerImplInstance(implClassName);
      return healerImpl.heal(healParameters);

    } catch (Exception e) {
      MdcDataErrorMessage.createErrorMessageAndUpdateMdc(LoggerConstants.TARGET_ENTITY_DB,
          LoggerTragetServiceName.SELF_HEALING, ErrorLevel.ERROR.name(), LoggerErrorCode
              .DATA_ERROR.getErrorCode(), LoggerErrorDescription.CANT_HEAL);
      throw new RuntimeException(String.format(Messages.CANT_LOAD_CLASS.getErrorMessage(),
          implClassName, e.getMessage()));
    }
  }

  @Override
  public void healAll(Map<String, Object> healParameters) {
    for (String implClassName : healerCodeToImplClass.values()) {
      try {
        Healer healerImpl = getHealerImplInstance(implClassName);
        healerImpl.heal(healParameters);
      } catch (Exception e) {
        MdcDataErrorMessage.createErrorMessageAndUpdateMdc(LoggerConstants.TARGET_ENTITY_DB,
            LoggerTragetServiceName.SELF_HEALING, ErrorLevel.ERROR.name(), LoggerErrorCode
                .DATA_ERROR.getErrorCode(), LoggerErrorDescription.CANT_HEAL);
        throw new RuntimeException(String.format(Messages.CANT_LOAD_CLASS.getErrorMessage(),
            implClassName, e.getMessage()));
      }
    }
  }

  private static Map<String, String> initHealers() {
    return FileUtils.readViaInputStream(HEALING_CONF_FILE, stream -> JsonUtil.json2Object(stream, Map.class));
  }

  private Healer getHealerImplInstance(String implClassName)
      throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
      IllegalAccessException, java.lang.reflect.InvocationTargetException {
    Class<?> clazz = Class.forName(implClassName);
    Constructor<?> constructor = clazz.getConstructor();
    return (Healer) constructor.newInstance();
  }
}
