package de.tbosch.commons.dao.jpa;

import de.tbosch.commons.dao.model.Person;
import de.tbosch.commons.persistence.dao.standard.StandardGenericJpaDao;

public class StandardPersonDao extends StandardGenericJpaDao<Person, Integer> implements PersonDao {

	public StandardPersonDao() {
		super(Person.class);
	}

}
