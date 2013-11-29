package de.tbosch.tools.googleapps.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;

import de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao;
import de.tbosch.tools.googleapps.dao.GEmailDao;
import de.tbosch.tools.googleapps.dao.GReminderDao;
import de.tbosch.tools.googleapps.exception.GoogleAppsException;
import de.tbosch.tools.googleapps.model.GCalendarEvent;
import de.tbosch.tools.googleapps.model.GEmail;
import de.tbosch.tools.googleapps.model.GReminder;
import de.tbosch.tools.googleapps.oauth2.OAuth2SaslClientFactory;
import de.tbosch.tools.googleapps.service.GoogleAppsService;
import de.tbosch.tools.googleapps.service.OAuth2Authenticator;
import de.tbosch.tools.googleapps.service.PreferencesService;
import de.tbosch.tools.googleapps.service.PreferencesService.PrefKey;
import de.tbosch.tools.googleapps.service.listeners.UpdateListener;

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
	private GEmailDao emailDao;

	@Autowired
	private PreferencesService preferencesService;

	@Autowired
	private OAuth2Authenticator oauth2Authenticator;

	private boolean connected = false;

	private final Set<UpdateListener> updateListeners = new HashSet<UpdateListener>();

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#updateCalendar()
	 */
	@Override
	public void updateCalendar() throws GoogleAppsException {
		if (connected) {
			// Iterate over all Events
			boolean updated = false;
			for (Event event : getPrimaryCalendarEvents()) {
				GCalendarEvent gEntry = new GCalendarEvent(event);
				GCalendarEvent like = calendarEventEntryDao.findLike(gEntry);
				if (like == null) {
					calendarEventEntryDao.create(gEntry);
					if (LOG.isDebugEnabled()) {
						LOG.debug("CalendarEvent with title " + gEntry.getTitle() + " was created.");
					}
					if (!event.getReminders().isEmpty() && event.getReminders().getOverrides() != null) {
						for (EventReminder reminder : event.getReminders().getOverrides()) {
							GReminder gReminder = new GReminder(reminder, gEntry);
							gEntry.getReminders().add(gReminder);

							// persist
							reminderDao.create(gReminder);
						}
						calendarEventEntryDao.update(gEntry);
						updated = true;
					} else {
						if (LOG.isDebugEnabled()) {
							LOG.debug("CalendarEvent with title " + gEntry.getTitle() + " has no reminders.");
						}
					}
				} else {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
					String starttime = dateFormat.format(gEntry.getStartTime());
					String endtime = dateFormat.format(gEntry.getEndTime());
					LOG.debug("CalendarEvent with title '" + gEntry.getTitle() + "' and starttime/endtime '"
							+ starttime + "/" + endtime + "' already exists.");
				}
			}
			if (updated) {
				for (UpdateListener updateListener : updateListeners) {
					updateListener.updated();
				}
			}
		} else {
			throw new IllegalStateException("not connected");
		}
	}

	/**
	 * Gets all events that are associated to the primary calendar.
	 * 
	 * @return List.
	 * @throws GoogleAppsException
	 */
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
	public List<GCalendarEvent> getAllCalendarEvents() {
		List<GCalendarEvent> list = calendarEventEntryDao.findAll();
		Collections.sort(list);
		return list;
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#getCalendarEventsFromNowOn()
	 */
	@Override
	public List<GCalendarEvent> getCalendarEventsFromNowOn() {
		List<GCalendarEvent> list = calendarEventEntryDao.findWithStarttimeAfterOrEqual(new Date());
		Collections.sort(list);
		return list;
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#connect()
	 */
	@Override
	public void connect() throws GoogleAppsException {
		try {
			oauth2Authenticator.authorize();
		} catch (Exception e) {
			connected = false;
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
		try {
			return connected && oauth2Authenticator.authorize().getExpiresInSeconds() > 0;
		} catch (Exception e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Not connected, because authorization failed", e);
			}
			return false;
		}
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
			imapStore = connectToImap("imap.gmail.com", 993, username, credential.getAccessToken(),
					LOG.isDebugEnabled());
		} catch (MessagingException e) {
			throw new GoogleAppsException("Failed to connect to IMAP server", e);
		}

		// Read inbox to get emails
		List<GEmail> createList = new ArrayList<GEmail>();
		try {
			Folder inbox = imapStore.getFolder("inbox");
			if (inbox.getMessageCount() > 0) {
				inbox.open(Folder.READ_ONLY);
				for (Message message : inbox.getMessages()) {
					GEmail email = new GEmail();
					email.setFromName(((InternetAddress) message.getFrom()[0]).getPersonal());
					email.setFromAddress(((InternetAddress) message.getFrom()[0]).getAddress());
					email.setSentDate(message.getSentDate());
					email.setSubject(message.getSubject());
					if (isTextContent(message)) {
						email.setContent((String) message.getContent());
					} else if (message.getContentType().startsWith("multipart")) {
						Multipart multipart = (Multipart) message.getContent();
						for (int i = 0; i < multipart.getCount(); i++) {
							BodyPart part = multipart.getBodyPart(i);
							if (isTextContent(part)) {
								email.setContent((String) part.getContent());
							} else {
								if (LOG.isDebugEnabled()) {
									LOG.debug("Multipart content type not saved: " + part.getContentType());
								}
							}
						}
					} else {
						if (LOG.isWarnEnabled()) {
							LOG.warn("Unknown content type: " + message.getContentType());
						}
					}
					List<GEmail> list = emailDao.findByExample(email);
					if (list.isEmpty()) {
						createList.add(email);
					} else {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Email already saved in database: " + email);
						}
					}
				}
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("INBOX is empty");
				}
			}
		} catch (IOException | MessagingException e) {
			throw new GoogleAppsException("Failed to get INBOX folder", e);
		}

		// Compare actual email-list in inbox with the database
		if (!CollectionUtils.isEqualCollection(emailDao.findAll(), createList)) {
			// First delete database to get a fresh copy of the INBOX
			for (GEmail email : emailDao.findAll()) {
				emailDao.delete(email);
			}

			// Create new list
			for (GEmail email : createList) {
				emailDao.create(email);
			}

			// inform listeners
			for (UpdateListener updateListener : updateListeners) {
				updateListener.updated();
			}
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("INBOX is unchanged");
			}
		}
	}

	/**
	 * Checks, if the email part's content is a text content.
	 * 
	 * @param part
	 *            The email part
	 * @return text content?
	 * @throws MessagingException
	 */
	private boolean isTextContent(Part part) throws MessagingException {
		return StringUtils.startsWithIgnoreCase(part.getContentType(), "text/html")
				|| StringUtils.startsWithIgnoreCase(part.getContentType(), "text/rtf")
				|| StringUtils.startsWithIgnoreCase(part.getContentType(), "text/plain");
	}

	/**
	 * @see de.tbosch.tools.googleapps.service.GoogleAppsService#getEmails()
	 */
	@Override
	public List<GEmail> getEmails() {
		List<GEmail> emails = emailDao.findAll();
		Collections.sort(emails);
		return emails;
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
