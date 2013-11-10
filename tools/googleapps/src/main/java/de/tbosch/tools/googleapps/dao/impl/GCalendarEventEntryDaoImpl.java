package de.tbosch.tools.googleapps.dao.impl;

import java.util.List;

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

	/**
	 * @see de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao#findLike(de.tbosch.tools.googleapps.model.GCalendarEventEntry)
	 */
	@Override
	public GCalendarEventEntry findLike(GCalendarEventEntry gEntry) {
		List list = getHibernateTemplate().findByExample(gEntry);
		return list.isEmpty() ? null : (GCalendarEventEntry)list.get(0);
	}

}
