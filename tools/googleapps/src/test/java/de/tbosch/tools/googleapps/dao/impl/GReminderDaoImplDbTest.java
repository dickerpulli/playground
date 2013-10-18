package de.tbosch.tools.googleapps.dao.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tbosch.tools.googleapps.AbstractSpringDbTest;
import de.tbosch.tools.googleapps.dao.GReminderDao;
import de.tbosch.tools.googleapps.model.GReminder;

public class GReminderDaoImplDbTest extends AbstractSpringDbTest {

	@Autowired
	private GReminderDao reminderDao;

	@Before
	public void before() throws IOException {
		executeSql("database/delete-tables.sql");
		executeSql("database/dao/GReminderDaoImplDbTest.sql");
	}

	@Test
	public void testRead() {
		GReminder reminder = reminderDao.read(1L);
		assertEquals(1, reminder.getId());
	}

}
