package org.openecomp.sdc.be.resources.data;

import org.junit.Test;
import org.openecomp.sdc.be.datatypes.elements.RelationshipInstDataDefinition;

import java.util.HashMap;
import java.util.Map;


public class RelationshipTypeDataTest {

	private RelationshipTypeData createTestSubject() {
		return new RelationshipTypeData();
	}

	@Test
	public void testCtor() throws Exception {
		new RelationshipTypeData(new HashMap<>());
		new RelationshipTypeData(new RelationshipInstDataDefinition());
	}
	
	@Test
	public void testToGraphMap() throws Exception {
		RelationshipTypeData testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toGraphMap();
	}

	
	@Test
	public void testGetRelationshipTypeDataDefinition() throws Exception {
		RelationshipTypeData testSubject;
		RelationshipInstDataDefinition result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelationshipTypeDataDefinition();
	}

	
	@Test
	public void testSetRelationshipTypeDataDefinition() throws Exception {
		RelationshipTypeData testSubject;
		RelationshipInstDataDefinition relationshipTypeDataDefinition = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRelationshipTypeDataDefinition(relationshipTypeDataDefinition);
	}

	
	@Test
	public void testGetUniqueId() throws Exception {
		RelationshipTypeData testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getUniqueId();
	}

	
	@Test
	public void testToString() throws Exception {
		RelationshipTypeData testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toString();
	}
}