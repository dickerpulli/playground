package de.tbosch.tools.googleapps.dao.impl;

import org.springframework.stereotype.Repository;

import de.tbosch.commons.persistence.dao.standard.StandardGenericHibernateDao;
import de.tbosch.tools.googleapps.dao.GEmailDao;
import de.tbosch.tools.googleapps.model.GEmail;

@Repository
public class GEmailDaoImpl extends StandardGenericHibernateDao<GEmail, Long> implements GEmailDao {

	public GEmailDaoImpl() {
		super(GEmail.class);
	}

}
