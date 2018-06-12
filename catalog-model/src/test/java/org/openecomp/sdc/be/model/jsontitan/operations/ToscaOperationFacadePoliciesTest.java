package org.openecomp.sdc.be.model.jsontitan.operations;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openecomp.sdc.be.dao.config.TitanSpringConfig;
import org.openecomp.sdc.be.dao.jsongraph.GraphVertex;
import org.openecomp.sdc.be.dao.jsongraph.TitanDao;
import org.openecomp.sdc.be.dao.jsongraph.types.VertexTypeEnum;
import org.openecomp.sdc.be.dao.titan.TitanOperationStatus;
import org.openecomp.sdc.be.datatypes.elements.PolicyTargetType;
import org.openecomp.sdc.be.datatypes.enums.ComponentTypeEnum;
import org.openecomp.sdc.be.datatypes.enums.GraphPropertyEnum;
import org.openecomp.sdc.be.datatypes.enums.JsonPresentationFields;
import org.openecomp.sdc.be.model.Component;
import org.openecomp.sdc.be.model.ComponentParametersView;
import org.openecomp.sdc.be.model.ModelTestBase;
import org.openecomp.sdc.be.model.PolicyDefinition;
import org.openecomp.sdc.be.model.config.ModelOperationsSpringConfig;
import org.openecomp.sdc.be.model.operations.api.StorageOperationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fj.data.Either;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TitanSpringConfig.class, ModelOperationsSpringConfig.class})
public class ToscaOperationFacadePoliciesTest extends ModelTestBase {

    private static final String CONTAINER_ID = "containerId";
    private static final String CONTAINER_NAME = "containerName";
    @Autowired
    private ToscaOperationFacade toscaOperationFacade;
    @Autowired
    private TitanDao titanDao;

    private PolicyDefinition policy1, policy2;

    @BeforeClass
    public static void setupBeforeClass() {
        ModelTestBase.init();
    }

    @Before
    public void setUp() throws Exception {
        policy1 = createPolicyDefinition("type1");
        policy2 = createPolicyDefinition("type2");
        createContainerVertexInDB();
        createPoliciesOnGraph(policy1, policy2);
    }

    private void createPoliciesOnGraph(PolicyDefinition ... policies) {
        for (int i = 0; i < policies.length; i++) {
            PolicyDefinition policy = policies[i];
            Either<PolicyDefinition, StorageOperationStatus> createdPolicy = toscaOperationFacade.associatePolicyToComponent(CONTAINER_ID, policy, i);
            assertTrue(createdPolicy.isLeft());
        }
    }

    @After
    public void tearDown() {
        titanDao.rollback();
    }

    @Test
    public void updatePoliciesTargetsOfComponent_updateSinglePolicy() {
        List<String> updatedTargetIds = asList("instance1new", "instance2");
        PolicyDefinition originalPolicy2 = clonePolicyWithTargets(policy2);
        updatePolicyTypeTargetsIds(policy1, PolicyTargetType.COMPONENT_INSTANCES, updatedTargetIds);
        updatePolicyTypeTargetsIds(policy2, PolicyTargetType.COMPONENT_INSTANCES, updatedTargetIds);

        StorageOperationStatus storageOperationStatus = toscaOperationFacade.updatePoliciesOfComponent(CONTAINER_ID, singletonList(policy1));
        assertThat(storageOperationStatus).isEqualTo(StorageOperationStatus.OK);
        Component updatedComponent = fetchComponentFromDB();
        verifyPolicyTargets(updatedComponent.getPolicyById(policy1.getUniqueId()), policy1);
        verifyPolicyTargets(updatedComponent.getPolicyById(policy2.getUniqueId()), originalPolicy2);
    }

    @Test
    public void updatePoliciesTargetsOfComponent_updateMultiplePolicies() {
        List<String> updatedTargetIds = asList("instance1new", "instance2");
        updatePolicyTypeTargetsIds(policy1, PolicyTargetType.COMPONENT_INSTANCES, updatedTargetIds);
        updatePolicyTypeTargetsIds(policy2, PolicyTargetType.COMPONENT_INSTANCES, updatedTargetIds);
        StorageOperationStatus storageOperationStatus = toscaOperationFacade.updatePoliciesOfComponent(CONTAINER_ID, asList(policy1, policy2));
        assertThat(storageOperationStatus).isEqualTo(StorageOperationStatus.OK);
        Component updatedComponent = fetchComponentFromDB();
        verifyPolicyTargets(updatedComponent.getPolicyById(policy1.getUniqueId()), policy1);
        verifyPolicyTargets(updatedComponent.getPolicyById(policy2.getUniqueId()), policy2);
    }

    private PolicyDefinition clonePolicyWithTargets(PolicyDefinition policy) {
        PolicyDefinition originalPolicy = new PolicyDefinition(policy);
        Map<PolicyTargetType, List<String>> clonedTargetMap = policy.getTargets().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> new ArrayList<>(entry.getValue())));
        originalPolicy.setTargets(clonedTargetMap);
        return originalPolicy;
    }

    private void verifyPolicyTargets(PolicyDefinition updatedPolicy, PolicyDefinition expectedPolicy) {
        assertThat(updatedPolicy.getTargets())
                .isEqualTo(expectedPolicy.getTargets());
    }

    private void updatePolicyTypeTargetsIds(PolicyDefinition policy, PolicyTargetType targetType, List<String> updatedTargetIds) {
        policy.getTargets().put(targetType, updatedTargetIds);
    }

    private Component fetchComponentFromDB() {
        ComponentParametersView componentParametersView = new ComponentParametersView();
        componentParametersView.disableAll();
        componentParametersView.setIgnorePolicies(false);
        return toscaOperationFacade.getToscaElement(CONTAINER_ID, componentParametersView).left().value();
    }

    private void createContainerVertexInDB() {
        GraphVertex resource = new GraphVertex(VertexTypeEnum.TOPOLOGY_TEMPLATE);
        resource.addMetadataProperty(GraphPropertyEnum.UNIQUE_ID, CONTAINER_ID);
        resource.addMetadataProperty(GraphPropertyEnum.NAME, CONTAINER_NAME);
        resource.setJsonMetadataField(JsonPresentationFields.NAME, CONTAINER_NAME);
        resource.setJsonMetadataField(JsonPresentationFields.COMPONENT_TYPE, ComponentTypeEnum.RESOURCE.name());
        Either<GraphVertex, TitanOperationStatus> container = titanDao.createVertex(resource);
        assertTrue(container.isLeft());
    }

    private PolicyDefinition createPolicyDefinition(String type) {
        PolicyDefinition policy = new PolicyDefinition();
        policy.setPolicyTypeName(type);
        policy.setTargets(new HashMap<>());
        policy.getTargets().put(PolicyTargetType.COMPONENT_INSTANCES, asList("instance1", "instance2"));
        return policy;
    }
}
