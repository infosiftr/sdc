package org.openecomp.sdc.be.model;

import org.junit.Test;
import org.openecomp.sdc.be.datatypes.elements.PropertyDataDefinition;
import org.openecomp.sdc.be.datatypes.elements.PropertyRule;

import java.util.List;


public class ComponentInstancePropertyTest {

	private ComponentInstanceProperty createTestSubject() {
		return new ComponentInstanceProperty();
	}

	@Test
	public void testCtor() throws Exception {
		new ComponentInstanceProperty(new PropertyDataDefinition());
		new ComponentInstanceProperty(new PropertyDefinition());
		new ComponentInstanceProperty(false, new PropertyDefinition(), "mock");
		new ComponentInstanceProperty(new PropertyDefinition(), "mock", "mock");
	}
	
	@Test
	public void testGetComponentInstanceName() throws Exception {
		ComponentInstanceProperty testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getComponentInstanceName();
	}

	
	@Test
	public void testSetComponentInstanceName() throws Exception {
		ComponentInstanceProperty testSubject;
		String componentInstanceName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setComponentInstanceName(componentInstanceName);
	}

	
	@Test
	public void testGetComponentInstanceId() throws Exception {
		ComponentInstanceProperty testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getComponentInstanceId();
	}

	
	@Test
	public void testSetComponentInstanceId() throws Exception {
		ComponentInstanceProperty testSubject;
		String componentInstanceId = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setComponentInstanceId(componentInstanceId);
	}

	
	@Test
	public void testGetValueUniqueUid() throws Exception {
		ComponentInstanceProperty testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getValueUniqueUid();
	}

	
	@Test
	public void testSetValueUniqueUid() throws Exception {
		ComponentInstanceProperty testSubject;
		String valueUniqueUid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setValueUniqueUid(valueUniqueUid);
	}

	
	@Test
	public void testGetPath() throws Exception {
		ComponentInstanceProperty testSubject;
		List<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getPath();
	}

	
	@Test
	public void testSetPath() throws Exception {
		ComponentInstanceProperty testSubject;
		List<String> path = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setPath(path);
	}

	
	@Test
	public void testGetRules() throws Exception {
		ComponentInstanceProperty testSubject;
		List<PropertyRule> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRules();
	}

	
	@Test
	public void testSetRules() throws Exception {
		ComponentInstanceProperty testSubject;
		List<PropertyRule> rules = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRules(rules);
	}

	
	@Test
	public void testToString() throws Exception {
		ComponentInstanceProperty testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toString();
	}
}