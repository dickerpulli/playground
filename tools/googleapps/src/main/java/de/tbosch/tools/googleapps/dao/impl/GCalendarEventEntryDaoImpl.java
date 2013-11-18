package de.tbosch.tools.googleapps.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
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
		return list.isEmpty() ? null : (GCalendarEventEntry) list.get(0);
	}

	/**
	 * @see de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao#findWithStarttimeAfterOrEqual(java.util.Date)
	 */
	@Override
	public List<GCalendarEventEntry> findWithStarttimeAfterOrEqual(Date date) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GCalendarEventEntry.class).add(
				Property.forName("startTime").ge(date));
		return getHibernateTemplate().findByCriteria(criteria);
	}

}