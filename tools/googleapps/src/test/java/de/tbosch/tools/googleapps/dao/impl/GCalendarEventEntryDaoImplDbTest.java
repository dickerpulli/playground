package de.tbosch.tools.googleapps.dao.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tbosch.tools.googleapps.AbstractSpringDbTest;
import de.tbosch.tools.googleapps.dao.GCalendarEventEntryDao;
import de.tbosch.tools.googleapps.model.GCalendarEventEntry;

public class GCalendarEventEntryDaoImplDbTest extends AbstractSpringDbTest {

	@Autowired
	private GCalendarEventEntryDao calendarEventEntryDao;

	@Before
	public void before() throws IOException {
		executeSql("database/delete-tables.sql");
		executeSql("database/dao/GCalendarEventEntryDaoImplDbTest.sql");
	}

	@Test
	public void testRead() {
		GCalendarEventEntry entry = calendarEventEntryDao.read(1L);
		assertEquals(1, entry.getId());
	}

}
