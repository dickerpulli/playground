package de.tbosch.tools.googleapps.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.property.DtStart;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;

public class RecurrenceUtilsTest {

	@Test
	public void testGetCalendarFromRecurrence() {
		String yearly = "BEGIN:VCALENDAR\n" + "DTSTART;VALUE=DATE:19790707\n" + "DTEND;VALUE=DATE:19790708\n"
				+ "RRULE:FREQ=YEARLY;BYMONTHDAY=7;BYMONTH=7\n" + "END:VCALENDAR";
		Calendar calendar = RecurrenceUtils.getCalendarFromRecurrence(yearly);
		assertNotNull(calendar);
		DtStart dtStart = (DtStart)calendar.getProperty(Property.DTSTART);
		assertEquals(
				DateTimeZone.forID("Europe/Berlin").convertUTCToLocal(
						new LocalDate(1979, 7, 7).toDateTimeAtStartOfDay().getMillis()), dtStart.getDate().getTime());
	}

	@Test
	public void testGetRecurFromRecurrence() {
		String yearly = "FREQ=YEARLY;BYMONTHDAY=7;BYMONTH=7";
		Recur recur = RecurrenceUtils.getRecurFromRecurrence(yearly);
		assertNotNull(recur);
		assertEquals(Recur.YEARLY, recur.getFrequency());
		assertEquals(1, recur.getMonthDayList().size());
		assertEquals(7, recur.getMonthDayList().get(0));
		assertEquals(1, recur.getMonthList().size());
		assertEquals(7, recur.getMonthList().get(0));
	}

}
