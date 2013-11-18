<<<<<<< HEAD
package de.tbosch.tools.googleapps.dao.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.joda.time.LocalDate;
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

	@Test
	public void testFindLike() throws Exception {
		GCalendarEventEntry entry = new GCalendarEventEntry();
		entry.setTitle("entry 1");
		entry.setStartTime(new LocalDate(2009, 1, 1).toDateTimeAtStartOfDay().toDate());
		entry.setEndTime(new LocalDate(2009, 1, 2).toDateTimeAtStartOfDay().toDate());
		GCalendarEventEntry like = calendarEventEntryDao.findLike(entry);
		assertEquals(1, like.getId());
	}

	@Test
	public void testFindWithStarttimeAfterOrEqual() throws Exception {
		List<GCalendarEventEntry> list = calendarEventEntryDao.findWithStarttimeAfterOrEqual(new LocalDate(2009, 3, 1)
				.toDateTimeAtStartOfDay().toDate());
		assertEquals(1, list.size());
		assertEquals(3, list.get(0).getId());
	}
}
=======
package de.tbosch.tools.googleapps.dao.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.joda.time.LocalDate;
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

	@Test
	public void testFindLike() throws Exception {
		GCalendarEventEntry entry = new GCalendarEventEntry();
		entry.setTitle("entry 1");
		entry.setStartTime(new LocalDate(2009, 1, 1).toDateTimeAtStartOfDay().toDate());
		entry.setEndTime(new LocalDate(2009, 1, 2).toDateTimeAtStartOfDay().toDate());
		GCalendarEventEntry like = calendarEventEntryDao.findLike(entry);
		assertEquals(1, like.getId());
	}

	@Test
	public void testFindWithStarttimeAfterOrEqual() throws Exception {
		List<GCalendarEventEntry> list = calendarEventEntryDao.findWithStarttimeAfterOrEqual(new LocalDate(2009, 3, 1)
				.toDateTimeAtStartOfDay().toDate());
		assertEquals(1, list.size());
		assertEquals(3, list.get(0).getId());
	}
}
>>>>>>> refs/remotes/origin/master
