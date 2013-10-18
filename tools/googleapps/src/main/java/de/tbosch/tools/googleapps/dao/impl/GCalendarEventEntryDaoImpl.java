package de.tbosch.tools.googleapps.dao.impl;

import org.springframework.stereotype.Repository;

import de.tbosch.commons.persistence.dao.standard.StandardGenericHibernateDao;
import de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao;
import de.tbosch.tools.googleapps.model.GCalendarEventEntry;

@Repository
public class GCalendarEventEntryDaoImpl extends StandardGenericHibernateDao<GCalendarEventEntry, Long> implements
		GCalendarEventEntryDao {

	public GCalendarEventEntryDaoImpl() {
		super(GCalendarEventEntry.class);
	}

}
