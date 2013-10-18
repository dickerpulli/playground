package de.tbosch.commons.dao.hibernate;

import de.tbosch.commons.dao.jpa.PersonDao;
import de.tbosch.commons.dao.model.Person;
import de.tbosch.commons.persistence.dao.standard.StandardGenericHibernateDao;

public class StandardPersonDao extends StandardGenericHibernateDao<Person, Integer> implements PersonDao {

	public StandardPersonDao() {
		super(Person.class);
	}

}
