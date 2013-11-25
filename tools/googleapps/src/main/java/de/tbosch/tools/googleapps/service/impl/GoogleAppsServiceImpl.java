package de.tbosch.tools.googleapps.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.util.AuthenticationException;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;

import de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao;
import de.tbosch.tools.googleapps.dao.GReminderDao;
import de.tbosch.tools.googleapps.exception.GoogleAppsException;
import de.tbosch.tools.googleapps.model.GCalendarEventEntry;
import de.tbosch.tools.googleapps.model.GReminder;
import de.tbosch.tools.googleapps.oauth2.OAuth2SaslClientFactory;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.OAuth2Authenticator;
import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.service.PreferencesService.PrefKey;
import de.tbosch.tools.googleapps.service.listeners.UpdateListener;
import edu.emory.mathcs.backport.java.util.Collections;

@Service
@Transactional
public class GoogleAppsServiceImpl implements GoogleAppsService {

	private static final Log LOG = LogFactory.getLog(GoogleAppsServiceImpl.class);

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	@Autowired
	private GReminderDao reminderDao;

	@Autowired
	private GCalendarEventEntryDao calendarEventEntryDao;

	@Autowired
	private CalendarService calendarService;

	@Autowired
	private PreferencesService preferencesService;

	@Autowired
	private OAuth2Authenticator oauth2Authenticator;

	private boolean connected = false;

	private final Set<UpdateListener> updateListeners = new HashSet<UpdateListener>();

	/**
	 * @throws ServiceException
	 * @throws IOException
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#updateCalendar()
	 */
	@Override
	public void updateCalendar() throws GoogleAppsException {
		if (connected) {
			// Iterate over all Events
			for (Event event : getPrimaryCalendarEvents()) {
				GCalendarEventEntry gEntry = new GCalendarEventEntry(event);
				GCalendarEventEntry like = calendarEventEntryDao.findLike(gEntry);
				if (like == null) {
					calendarEventEntryDao.create(gEntry);
					if (LOG.isDebugEnabled()) {
						LOG.debug("CalendarEvent with title " + gEntry.getTitle() + " was created.");
					}
					if (!event.getReminders().isEmpty()) {
						for (EventReminder reminder : event.getReminders().getOverrides()) {
							GReminder gReminder = new GReminder(reminder, gEntry);
							gEntry.getReminders().add(gReminder);

							// persist
							reminderDao.create(gReminder);
						}
						calendarEventEntryDao.update(gEntry);
					} else {
						if (LOG.isDebugEnabled()) {
							LOG.debug("CalendarEvent with title " + gEntry.getTitle() + " has no reminders.");
						}
					}
					for (UpdateListener updateListener : updateListeners) {
						updateListener.updated();
					}
				} else {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
					String starttime = dateFormat.format(gEntry.getStartTime());
					String endtime = dateFormat.format(gEntry.getEndTime());
					LOG.debug("CalendarEvent with title '" + gEntry.getTitle() + "' and starttime/endtime '"
							+ starttime + "/" + endtime + "' already exists.");
				}
			}
		} else {
			throw new IllegalStateException("not connected");
		}
	}

	private List<Event> getPrimaryCalendarEvents() throws GoogleAppsException {
		HttpTransport httpTransport;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			throw new GoogleAppsException("Error initializing transport layer", e);
		}
		Credential credential;
		try {
			credential = oauth2Authenticator.authorize();
		} catch (Exception e) {
			throw new GoogleAppsException("Authentication failed", e);
		}
		Calendar calendar = new Calendar.Builder(httpTransport, JSON_FACTORY, credential).build();
		Events events;
		try {
			events = calendar.events().list("primary").execute();
		} catch (IOException e) {
			throw new GoogleAppsException("Error getting primary calendar events", e);
		}
		return events.getItems();
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#getAllReminders()
	 */
	@Override
	public List<GReminder> getAllReminders() {
		List<GReminder> list = reminderDao.findAll();
		Collections.sort(list);
		return list;
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#getAllCalendarEvents()
	 */
	@Override
	public List<GCalendarEventEntry> getAllCalendarEvents() {
		List<GCalendarEventEntry> list = calendarEventEntryDao.findAll();
		Collections.sort(list);
		return list;
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#getCalendarEventsFromNowOn()
	 */
	@Override
	public List<GCalendarEventEntry> getCalendarEventsFromNowOn() {
		List<GCalendarEventEntry> list = calendarEventEntryDao.findWithStarttimeAfterOrEqual(new Date());
		Collections.sort(list);
		return list;
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#connect()
	 */
	@Override
	public void connect() throws GoogleAppsException {
		String username = preferencesService.readPref(PrefKey.USERNAME);
		String password = preferencesService.readPref(PrefKey.PASSWORD);

		// Set up Google Apps service
		try {
			calendarService.setUserCredentials(username, password);
		} catch (AuthenticationException e) {
			throw new GoogleAppsException("Authentication failed", e);
		}
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

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#addUpdateListener(de.tbosch.tools.googleapps.service.impl.GoogleAppsServiceImpl.UpdateListener)
	 */
	@Override
	public void addUpdateListener(UpdateListener updateListener) {
		updateListeners.add(updateListener);
	}

	/**
	 * @throws Exception
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#updateEmails()
	 */
	@Override
	public void updateEmails() throws GoogleAppsException {
		String username = preferencesService.readPref(PrefKey.USERNAME);
		Credential credential;
		try {
			credential = oauth2Authenticator.authorize();
		} catch (Exception e) {
			throw new GoogleAppsException("Authentication failed", e);
		}
		IMAPStore imapStore;
		try {
			imapStore = connectToImap("imap.gmail.com", 993, username, credential.getAccessToken(), true);
		} catch (MessagingException e) {
			throw new GoogleAppsException("Failed to connect to IMAP server", e);
		}
		try {
			System.out.println(imapStore.getFolder("inbox").getMessageCount());
		} catch (MessagingException e) {
			throw new GoogleAppsException("Failed to get INBOX folder", e);
		}
	}

	/**
	 * Connects and authenticates to an IMAP server with OAuth2. You must have called {@code initialize}.
	 * 
	 * @param host
	 *            Hostname of the imap server, for example {@code imap.googlemail.com}.
	 * @param port
	 *            Port of the imap server, for example 993.
	 * @param userEmail
	 *            Email address of the user to authenticate, for example {@code oauth@gmail.com}.
	 * @param oauthToken
	 *            The user's OAuth token.
	 * @param debug
	 *            Whether to enable debug logging on the IMAP connection.
	 * 
	 * @return An authenticated IMAPStore that can be used for IMAP operations.
	 */
	private IMAPStore connectToImap(String host, int port, String userEmail, String oauthToken, boolean debug)
			throws MessagingException {
		Properties props = new Properties();
		props.put("mail.imaps.sasl.enable", "true");
		props.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);
		Session session = Session.getInstance(props);
		session.setDebug(debug);

		final URLName unusedUrlName = null;
		IMAPSSLStore store = new IMAPSSLStore(session, unusedUrlName);
		final String emptyPassword = "";
		store.connect(host, port, userEmail, emptyPassword);
		return store;
	}

}
