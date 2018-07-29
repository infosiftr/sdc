package org.openecomp.sdc.be.model;

import org.openecomp.sdc.be.datatypes.elements.CapabilityDataDefinition;
import org.openecomp.sdc.be.datatypes.elements.RequirementDataDefinition;
/**
 * Contains the Capability, Requirement and Relationship info
 */
public class CapabilityRequirementRelationship {

    private RelationshipInfo relation;
    private CapabilityDataDefinition capability;
    private RequirementDataDefinition requirement;

    public RelationshipInfo getRelation() {
        return relation;
    }
    public void setRelation(RelationshipInfo relation) {
        this.relation = relation;
    }
    public CapabilityDataDefinition getCapability() {
        return capability;
    }
    public void setCapability(CapabilityDataDefinition capability) {
        this.capability = capability;
    }
    public RequirementDataDefinition getRequirement() {
        return requirement;
    }
    public void setRequirement(RequirementDataDefinition requirement) {
        this.requirement = requirement;
    }
}
