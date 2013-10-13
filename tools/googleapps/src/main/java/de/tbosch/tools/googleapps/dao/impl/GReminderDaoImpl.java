package de.tbosch.tools.googleapps.dao.impl;

import org.springframework.stereotype.Repository;

import de.tbosch.commons.persistence.dao.standard.StandardGenericHibernateDao;
import de.tbosch.tools.googleapps.dao.GReminderDao;
import de.tbosch.tools.googleapps.model.GReminder;

@Repository
public class GReminderDaoImpl extends StandardGenericHibernateDao<GReminder, Long> implements GReminderDao {

	public GReminderDaoImpl() {
		super(GReminder.class);
	}

}
