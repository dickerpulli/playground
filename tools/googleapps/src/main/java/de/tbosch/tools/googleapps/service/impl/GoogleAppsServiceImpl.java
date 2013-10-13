package de.tbosch.tools.googleapps.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.util.ServiceException;

import de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao;
import de.tbosch.tools.googleapps.dao.GReminderDao;
import de.tbosch.tools.googleapps.model.GCalendarEventEntry;
import de.tbosch.tools.googleapps.model.GReminder;
import de.tbosch.tools.googleapps.service.GCalendarService;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.PreferencesService;

@Service
@Transactional
public class GoogleAppsServiceImpl implements GoogleAppsService {

	@Autowired
	private GReminderDao reminderDao;

	@Autowired
	private GCalendarEventEntryDao calendarEventEntryDao;

	@Autowired
	private GCalendarService calendarService;

	@Autowired
	private PreferencesService preferencesService;

	/**
	 * @throws ServiceException
	 * @throws IOException
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#getAndSaveCalendar()
	 */
	@Override
	public void getAndSaveCalendar() throws IOException, ServiceException {
		try {
			String username = preferencesService.readUsername();
			String password = preferencesService.readPassword();

			// Set up Google Apps service
			calendarService.setUserCredentials(username, password);

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
				if (!entry.getReminder().isEmpty()) {
					GCalendarEventEntry gEntry = new GCalendarEventEntry(entry);
					calendarEventEntryDao.create(gEntry);
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
					System.out.println(entry.hasRepeatingExtension(null));
				}
			}

		}
		catch (MalformedURLException e) {
			throw new IllegalArgumentException("Should not happen, look in the code ...");
		}
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#getAllReminders()
	 */
	@Override
	public List<GReminder> getAllReminders() {
		return reminderDao.findAll();
	}

}
