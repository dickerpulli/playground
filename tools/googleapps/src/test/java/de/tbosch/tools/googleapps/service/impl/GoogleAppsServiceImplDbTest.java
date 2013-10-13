package de.tbosch.tools.googleapps.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gdata.client.Query;
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
import de.tbosch.tools.googleapps.service.GCalendarService;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.PreferencesService;

public class GoogleAppsServiceImplDbTest extends AbstractSpringDbTest {

	@Autowired
	private GoogleAppsService googleAppsService;

	@Autowired
	private GCalendarService calendarServiceMock;

	@Autowired
	private PreferencesService preferencesServiceMock;

	@Before
	public void before() throws IOException {
		executeSql("database/delete-tables.sql");
		executeSql("database/service/GoogleAppsServiceImplDbTest.sql");

		EasyMock.reset(calendarServiceMock, preferencesServiceMock);
	}

	@Test
	public void testGetAndSaveCalendar() throws IOException, ServiceException {
		expect(preferencesServiceMock.readUsername()).andReturn("usr");
		expect(preferencesServiceMock.readPassword()).andReturn("pwd");
		calendarServiceMock.setUserCredentials("usr", "pwd");
		expectLastCall();
		expect(calendarServiceMock.getFeed(isA(Query.class), isA(Class.class))).andReturn(getFeed());
		replay(calendarServiceMock, preferencesServiceMock);

		googleAppsService.getAndSaveCalendar();
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
