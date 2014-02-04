package de.tbosch.commons.dao.hibernate;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.dbunit.DatabaseUnitException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import de.tbosch.commons.dao.model.Person;
import de.tbosch.commons.persistence.dao.GenericDao;
import de.tbosch.commons.test.AbstractGenericDaoDbUnitTest;
import de.tbosch.commons.test.util.DataloadUtils;

@ContextConfiguration(locations = ("/applicationContextHibernate.xml"))
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class StandardPersonDaoITest extends AbstractGenericDaoDbUnitTest<Person, Integer> {

	@Resource(name = "personDao")
	private GenericDao<Person, Serializable> genericDao;

	public StandardPersonDaoITest() {
		type = Person.class;
		pkName = "Id";
		mergeValue = "fname";
		mergePropertyName = "Firstname";
	}

	@Before
	public void before() throws IOException, SQLException, DatabaseUnitException, URISyntaxException {
		executeSqlScript("/database/clear-tables.sql", ";");
		DataloadUtils.createDbUnitDtdFile("model", new String[] { "dev/", "src/test/resources/database/dev/" },
				new String[] { "t_person" });
		executeDbUnitXmlScript("/database/StandardPersonDaoITest.dbunit.xml");
	}

	@Test
	public void testReadPerson() {
		genericDao.read(1);
	}

	@Override
	public Person getCreateTestInstanz() {
		Person person = new Person();
		person.setId(null);
		person.setFirstname("firstname");
		person.setLastname("lastname");
		return person;
	}

	@Override
	public GenericDao<Person, Serializable> getGenericDao() {
		return genericDao;
	}

}
