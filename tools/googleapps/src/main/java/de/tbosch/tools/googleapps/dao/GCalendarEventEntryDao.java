package de.tbosch.tools.googleapps.dao;

import java.util.Date;
import java.util.List;

import de.tbosch.commons.persistence.dao.GenericDao;
import de.tbosch.tools.googleapps.model.GCalendarEvent;

public interface GCalendarEventEntryDao extends GenericDao<GCalendarEvent, Long> {

	/**
	 * Finds a calendar event entry that has the same information in title, starttime and endtime.
	 * 
	 * @param gEntry
	 *            The event entry to compare to.
	 * @return Probably an event entry from database.
	 */
	GCalendarEvent findLike(GCalendarEvent gEntry);

	/**
	 * Finds all calendar event enties that has a starttime that is on the date or in its future.
	 * 
	 * @param date
	 *            The day.
	 * @return List.
	 */
	List<GCalendarEvent> findWithStarttimeAfterOrEqual(Date date);

}
