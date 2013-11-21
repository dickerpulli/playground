package de.tbosch.tools.googleapps.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.IFeed;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.ServiceException;

import de.tbosch.tools.googleapps.AbstractSpringDbTest;
import de.tbosch.tools.googleapps.model.GReminder;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.service.PreferencesService.PrefKey;

public class GoogleAppsServiceImplDbTest extends AbstractSpringDbTest {

	@Autowired
	private GoogleAppsService googleAppsService;

	@Autowired
	private CalendarService calendarServiceMock;

	@Autowired
	private PreferencesService preferencesServiceMock;

	@Before
	public void before() throws IOException {
		executeSql("database/delete-tables.sql");
		executeSql("database/service/GoogleAppsServiceImplDbTest.sql");

		EasyMock.reset(calendarServiceMock, preferencesServiceMock);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateCalendar() throws IOException, ServiceException {
		expect(preferencesServiceMock.readPref(PrefKey.USERNAME)).andReturn("usr").times(2);
		expect(preferencesServiceMock.readPref(PrefKey.PASSWORD)).andReturn("pwd");
		calendarServiceMock.setUserCredentials("usr", "pwd");
		expectLastCall();
		expect(calendarServiceMock.getFeed(isA(Query.class), isA(Class.class))).andReturn(getFeed());
		replay(calendarServiceMock, preferencesServiceMock);

		googleAppsService.connect();
		googleAppsService.updateCalendar();
		List<GReminder> reminders = googleAppsService.getAllReminders();

		assertEquals(2, reminders.size());
		verify(calendarServiceMock, preferencesServiceMock);
	}

	private IFeed getFeed() {
		CalendarEventFeed feed = new CalendarEventFeed();
		List<CalendarEventEntry> list = new ArrayList<CalendarEventEntry>();
		CalendarEventEntry entry1 = new CalendarEventEntry();
		Reminder reminder1 = new Reminder();
		reminder1.setMinutes(10);
		entry1.setTitle(new PlainTextConstruct("Termin A"));
		When time = new When();
		time.setStartTime(new DateTime(new Date()));
		time.setEndTime(new DateTime(new Date()));
		time.addRepeatingExtension(reminder1);
		entry1.addTime(time);
		list.add(entry1);
		feed.setEntries(list);
		return feed;
	}

}
