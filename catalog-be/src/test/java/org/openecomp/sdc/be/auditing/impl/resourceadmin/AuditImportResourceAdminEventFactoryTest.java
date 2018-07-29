package org.openecomp.sdc.be.auditing.impl.resourceadmin;

import org.junit.Test;
import org.openecomp.sdc.be.model.User;
import org.openecomp.sdc.be.resources.data.auditing.model.CommonAuditData;
import org.openecomp.sdc.be.resources.data.auditing.model.ResourceCommonInfo;
import org.openecomp.sdc.be.resources.data.auditing.model.ResourceVersionInfo;

public class AuditImportResourceAdminEventFactoryTest {

	private AuditImportResourceAdminEventFactory createTestSubject() {
		CommonAuditData.Builder newBuilder = CommonAuditData.newBuilder();
		CommonAuditData commonAuData = newBuilder.build();
		ResourceVersionInfo.Builder newBuilder2 = ResourceVersionInfo.newBuilder();
		ResourceVersionInfo resAuData = newBuilder2.build();
		return new AuditImportResourceAdminEventFactory(commonAuData,new ResourceCommonInfo(), resAuData,resAuData,"", new User(),"");
	}

	@Test
	public void testGetLogMessage() throws Exception {
		AuditImportResourceAdminEventFactory testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getLogMessage();
	}
}