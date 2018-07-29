package org.openecomp.sdc.be.components.merge.instance;

import fj.data.Either;
import org.openecomp.sdc.be.components.impl.ArtifactsBusinessLogic;
import org.openecomp.sdc.be.components.merge.heat.HeatEnvArtifactsMergeBusinessLogic;
import org.openecomp.sdc.be.impl.ComponentsUtils;
import org.openecomp.sdc.be.model.*;
import org.openecomp.sdc.common.api.ArtifactGroupTypeEnum;
import org.openecomp.sdc.common.log.wrappers.Logger;
import org.openecomp.sdc.exception.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by chaya on 9/20/2017.
 */
@org.springframework.stereotype.Component("ComponentInstanceHeatEnvMerge")
public class ComponentInstanceHeatEnvMerge implements ComponentInstanceMergeInterface {

    private static final Logger log = Logger.getLogger(ComponentInstanceHeatEnvMerge.class);

    @Autowired
    private ArtifactsBusinessLogic artifactsBusinessLogic;

    @Autowired
    private HeatEnvArtifactsMergeBusinessLogic heatEnvArtifactsMergeBusinessLogic;

    @Autowired
    protected ComponentsUtils componentsUtils;


    @Override
    public void saveDataBeforeMerge(DataForMergeHolder dataHolder, Component containerComponent, ComponentInstance currentResourceInstance, Component originComponent) {
        dataHolder.setOrigComponentInstanceHeatEnvArtifacts(containerComponent.safeGetComponentInstanceHeatArtifacts(currentResourceInstance.getUniqueId()));
    }

    @Override
    public Either<Component, ResponseFormat> mergeDataAfterCreate(User user, DataForMergeHolder dataHolder, Component updatedContainerComponent, String newInstanceId) {
        List<ArtifactDefinition> origCompInstHeatEnvArtifacts = dataHolder.getOrigComponentInstanceHeatEnvArtifacts();
        List<ArtifactDefinition> newCompInstHeatEnvArtifacts = updatedContainerComponent.safeGetComponentInstanceHeatArtifacts(newInstanceId);
        List<ArtifactDefinition> artifactsToUpdate = heatEnvArtifactsMergeBusinessLogic.mergeInstanceHeatEnvArtifacts(origCompInstHeatEnvArtifacts, newCompInstHeatEnvArtifacts);

        for (ArtifactDefinition artifactInfo : artifactsToUpdate) {
            Map<String, Object> json = artifactsBusinessLogic.buildJsonForUpdateArtifact(artifactInfo, ArtifactGroupTypeEnum.DEPLOYMENT,  null);

            Either<Either<ArtifactDefinition, Operation>, ResponseFormat> uploadArtifactToService = artifactsBusinessLogic.updateResourceInstanceArtifactNoContent(newInstanceId, updatedContainerComponent, user, json,
                    artifactsBusinessLogic.new ArtifactOperationInfo(false, false, ArtifactsBusinessLogic.ArtifactOperationEnum.UPDATE), null);
            if (uploadArtifactToService.isRight()) {
                log.error("Failed to update artifact {} on resourceInstance {}", artifactInfo.getArtifactLabel(), newInstanceId);
                return Either.right(uploadArtifactToService.right().value());
            }
        }
        return Either.left(updatedContainerComponent);
    }
}
