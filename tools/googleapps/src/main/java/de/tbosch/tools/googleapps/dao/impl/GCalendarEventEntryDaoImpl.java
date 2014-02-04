package de.tbosch.tools.googleapps.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

import de.tbosch.commons.persistence.dao.standard.StandardGenericHibernateDao;
import de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao;
import de.tbosch.tools.googleapps.model.GCalendarEvent;

@Repository
public class GCalendarEventEntryDaoImpl extends StandardGenericHibernateDao<GCalendarEvent, Long> implements
		GCalendarEventEntryDao {

	public GCalendarEventEntryDaoImpl() {
		super(GCalendarEvent.class);
	}

	/**
	 * @see de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao#findLike(de.tbosch.tools.googleapps.model.GCalendarEvent)
	 */
	@Override
	public GCalendarEvent findLike(GCalendarEvent gEntry) {
		List<GCalendarEvent> list = findByExample(gEntry);
		return list.isEmpty() ? null : (GCalendarEvent) list.get(0);
	}

	/**
	 * @see de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao#findWithStarttimeAfterOrEqual(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GCalendarEvent> findWithStarttimeAfterOrEqual(Date date) {
		return getCurrentSession().createCriteria(GCalendarEvent.class)
				.add(Property.forName("startTime").ge(date)).list();
	}

}