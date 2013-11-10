package de.tbosch.tools.googleapps.dao;

import de.tbosch.commons.persistence.dao.GenericDao;
import de.tbosch.tools.googleapps.model.GCalendarEventEntry;

public interface GCalendarEventEntryDao extends GenericDao<GCalendarEventEntry, Long> {

	/**
	 * Finds a calendar event entry that has the same information in title, starttime and endtime.
	 * @param gEntry The event entry to compare to.
	 * @return Probably an event entry from database.
	 */
	GCalendarEventEntry findLike(GCalendarEventEntry gEntry);

}
