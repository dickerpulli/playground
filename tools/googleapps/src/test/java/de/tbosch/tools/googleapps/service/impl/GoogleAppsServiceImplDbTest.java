package de.tbosch.tools.googleapps.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tbosch.tools.googleapps.AbstractSpringDbTest;
import de.tbosch.tools.googleapps.model.GReminder;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.service.PreferencesService.PrefKey;

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
		expect(preferencesServiceMock.readPref(PrefKey.USERNAME)).andReturn("usr").times(2);
		expect(preferencesServiceMock.readPref(PrefKey.PASSWORD)).andReturn("pwd");
		expectLastCall();

		googleAppsService.connect();
		googleAppsService.updateCalendar();
		List<GReminder> reminders = googleAppsService.getAllReminders();

		assertEquals(2, reminders.size());
	}

}
