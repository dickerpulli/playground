package de.tbosch.tools.googleapps.service;

import java.io.IOException;
import java.util.List;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import de.tbosch.tools.googleapps.model.GReminder;

public interface GoogleAppsService {

	/**
	 * Gets the calendar from google and saves all entries in local database.
	 * @throws AuthenticationException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public void getAndSaveCalendar() throws IOException, ServiceException;

	/**
	 * All reminders in database.
	 * 
	 * @return The list
	 */
	public List<GReminder> getAllReminders();

}
