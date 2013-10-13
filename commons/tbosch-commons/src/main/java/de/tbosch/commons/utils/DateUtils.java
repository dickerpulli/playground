package de.tbosch.commons.utils;

import java.sql.Date;
import java.util.Calendar;

public class DateUtils {

	private DateUtils() {
		// utils
	}

	/**
	 * Zählt die Anzahl der Tage zwischen 2 Daten.
	 * 
	 * 
	 * @return Anzahl der Tage
	 */
	public static long daysBetween(java.util.Date eins, java.util.Date zwei) {
		// CSOFF: MagicNumberCheck
		if (eins == null || zwei == null)
			return 0;
		long ONE_HOUR = 60 * 60 * 1000L;
		return ((zwei.getTime() - eins.getTime() + ONE_HOUR) / (ONE_HOUR * 24));
		// CSON: MagicNumberCheck
	}

	/**
	 * Prüft, ob es sich beim Tag des Kalenders um einen Werktag handelt. Derzeit gilt jeder Tage von Montag bis Freitag
	 * als Werktag
	 * 
	 * @param calendar
	 *            Diesen 'calendar' prüfen
	 * 
	 * @return Werktag?
	 */
	public static boolean istWerktag(final Calendar calendar) {
		final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek == Calendar.SATURDAY) {
			return false;
		}

		if (dayOfWeek == Calendar.SUNDAY) {
			return false;
		}

		return true;
	}

	/**
	 * Liefert das aktuelle Systemdatum
	 * 
	 * @return aktuelle Systemzeit, nie 'null'
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

}
