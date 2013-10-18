package de.tbosch.tools.googleapps.utils;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.property.RRule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RecurrenceUtils {

	private static final Log LOG = LogFactory.getLog(RecurrenceUtils.class);

	private RecurrenceUtils() {
		// utils
	}

	public static Calendar getCalendarFromRecurrence(String calendarString) {
		StringReader sin = new StringReader(calendarString);
		CalendarBuilder builder = new CalendarBuilder();
		try {
			Calendar calendar = builder.build(sin);
			return calendar;
		}
		catch (IOException e) {
			LOG.error("Error in parsing iCal text string, return NULL", e);
		}
		catch (ParserException e) {
			LOG.error("Error in parsing iCal text string, return NULL", e);
		}
		return null;
	}

	public static Recur getRecurFromRecurrence(String calendarString) {
		try {
			return new RRule(null, calendarString).getRecur();
		}
		catch (ParseException e) {
			LOG.error("Error in parsing iCal text string, return NULL", e);
		}
		return null;
	}

	public static TimeZone getTimezone(String text) {
		return null;
	}

}
