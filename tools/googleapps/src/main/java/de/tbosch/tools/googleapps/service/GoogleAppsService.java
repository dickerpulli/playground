package de.tbosch.tools.googleapps.service;

import java.io.IOException;
import java.util.List;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import de.tbosch.tools.googleapps.model.GCalendarEventEntry;
import de.tbosch.tools.googleapps.model.GReminder;
import de.tbosch.tools.googleapps.service.listeners.UpdateListener;

public interface GoogleAppsService {

	/**
	 * Gets the calendar from google and saves all entries in local database.
	 * @throws AuthenticationException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public void updateCalendar() throws IOException, ServiceException;

	/**
	 * All reminders in database. In sort order.
	 * 
	 * @return The list
	 */
	public List<GReminder> getAllReminders();

	/**
	 * All calendar events. In sort order.
	 * @return all calendar events.
	 */
	public List<GCalendarEventEntry> getAllCalendarEvents();

	/**
	 * All calendar events. In sort order.
	 * @return all calendar events that start time are after or equal today.
	 */
	public List<GCalendarEventEntry> getCalendarEventsFromNowOn();

	/**
	 * Sets the authentication information to connect to google service.
	 * @throws ServiceException
	 */
	public void connect() throws ServiceException;

	/**
	 * Checks if service is connected to google, i.e. authentication is set.
	 * @return connected?
	 */
	public boolean isConnected();

	/**
	 * Resets the authentication information to disconnect from google service.
	 */
	public void disconnect();

	/**
	 * Adds update listeners to the internal set.
	 * @param updateListener The listener.
	 */
	void addUpdateListener(UpdateListener updateListener);

}
