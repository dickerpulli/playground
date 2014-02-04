package de.tbosch.tools.googleapps.service.impl;

import java.io.IOException;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tbosch.tools.googleapps.AbstractSpringDbTest;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.PreferencesService;

public class GoogleAppsServiceImplDbTest extends AbstractSpringDbTest {

	@Autowired
	private GoogleAppsService googleAppsService;

	@Autowired
	private PreferencesService preferencesServiceMock;

	@Before
	public void before() throws IOException {
		executeSql("database/delete-tables.sql");
		executeSql("database/service/GoogleAppsServiceImplDbTest.sql");

		EasyMock.reset(preferencesServiceMock);
	}

	@Test
	public void testUpdateCalendar() throws Exception {

	}

}
