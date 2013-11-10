package de.tbosch.tools.googleapps.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.util.ServiceException;

import de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao;
import de.tbosch.tools.googleapps.dao.GReminderDao;
import de.tbosch.tools.googleapps.model.GCalendarEventEntry;
import de.tbosch.tools.googleapps.model.GReminder;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.service.PreferencesService.PrefKey;

@Service
@Transactional
public class GoogleAppsServiceImpl implements GoogleAppsService {

	private static final Log LOG = LogFactory.getLog(GoogleAppsServiceImpl.class);

	@Autowired
	private GReminderDao reminderDao;

	@Autowired
	private GCalendarEventEntryDao calendarEventEntryDao;

	@Autowired
	private CalendarService calendarService;

	@Autowired
	private PreferencesService preferencesService;

	private boolean connected = false;

	/**
	 * @throws ServiceException
	 * @throws IOException
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#updateCalendar()
	 */
	@Override
	public void updateCalendar() throws IOException, ServiceException {
		if (connected) {
			try {
				String username = preferencesService.readPref(PrefKey.USERNAME);

				// Construct URL
				StringBuffer url = new StringBuffer();
				url.append("https://www.google.com/calendar/feeds/");
				url.append(username);
				url.append("/private/full");
				URL feedUrl = new URL(url.toString());

				// Query all events from the calendar
				CalendarQuery myQuery = new CalendarQuery(feedUrl);
				CalendarEventFeed queryResult = calendarService.getFeed(myQuery, CalendarEventFeed.class);
				List<CalendarEventEntry> entries = queryResult.getEntries();

				// Iterate over all Events
				for (CalendarEventEntry entry : entries) {
					GCalendarEventEntry gEntry = new GCalendarEventEntry(entry);
					GCalendarEventEntry like = calendarEventEntryDao.findLike(gEntry);
					if (like == null) {
						calendarEventEntryDao.create(gEntry);
						if (LOG.isDebugEnabled()) {
							LOG.debug("CalendarEvent with title " + gEntry.getTitle() + " was created.");
						}
						if (!entry.getReminder().isEmpty()) {
							for (Reminder reminder : entry.getReminder()) {
								if (reminder.getMinutes() <= 15 && reminder.getMinutes() >= 0) {
									GReminder gReminder = new GReminder(reminder, gEntry);
									gEntry.getReminders().add(gReminder);

									// persist
									reminderDao.create(gReminder);
								}
							}
							calendarEventEntryDao.update(gEntry);
						}
						else {
							if (LOG.isDebugEnabled()) {
								LOG.debug("CalendarEvent with title " + gEntry.getTitle() + " has no reminders.");
							}
						}
					}
					else {
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
						String starttime = dateFormat.format(gEntry.getStartTime());
						String endtime = dateFormat.format(gEntry.getEndTime());
						LOG.debug("CalendarEvent with title '" + gEntry.getTitle() + "' and starttime/endtime '"
								+ starttime + "/" + endtime + "' already exists.");
					}
				}

			}
			catch (MalformedURLException e) {
				throw new IllegalArgumentException("Should not happen, look in the code ...");
			}
		}
		else {
			throw new IllegalStateException("not connected");
		}
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#getAllReminders()
	 */
	@Override
	public List<GReminder> getAllReminders() {
		return reminderDao.findAll();
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#getAllCalendarEvents()
	 */
	@Override
	public List<GCalendarEventEntry> getAllCalendarEvents() {
		return calendarEventEntryDao.findAll();
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#connect()
	 */
	@Override
	public void connect() throws ServiceException {
		String username = preferencesService.readPref(PrefKey.USERNAME);
		String password = preferencesService.readPref(PrefKey.PASSWORD);

		// Set up Google Apps service
		calendarService.setUserCredentials(username, password);
		connected = true;
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#disconnect()
	 */
	@Override
	public void disconnect() {
		connected = false;
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return connected;
	}

}
