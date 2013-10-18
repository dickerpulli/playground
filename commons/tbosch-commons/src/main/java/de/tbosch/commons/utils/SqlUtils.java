package de.tbosch.commons.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SqlUtils {

	private SqlUtils() {
		// utils
	}

	/**
	 * Liefert die aktuelle Systemzeit
	 * 
	 * @return aktuelle Systemzeit, nie 'null'
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Liefert die aktuelle Systemzeit - ohne Millisekunden
	 * 
	 * @return aktuelle Systemzeit, nie 'null'
	 */
	public static Time getCurrentTime() {
		return new Time(System.currentTimeMillis());
	}

	/**
	 * Addiert eine Anzahl von Minuten zu einem Timestamp und gibt das Ergebnis in einem neuen Timestamp zurück. Der
	 * ursprüngliche Timestamp wird nicht veraendert.
	 * 
	 * @param original
	 *            Ursprünglicher Timestamp
	 * @param minuten
	 *            Anzahl Minuten, die hinzugefügt werden sollen
	 * 
	 * @return Neuer, veränderter Timestamp
	 */
	public static Timestamp addMinuten(final Timestamp original, final int minuten) {

		final long t1 = original.getTime();
		final int minInMs = 1000 * 60;
		final long t2 = t1 + ((long) minuten * minInMs);
		final Timestamp result = new Timestamp(t2);
		return result;
	}

	/**
	 * Addiert eine Anzahl an Werktagen zu einem Timestamp und gibt das Ergebnis in einem neuen Timestamp zurück. Der
	 * ursprüngliche Timestamp wird nicht veraendert. Als Werktag gilt jeder Tag von Montag bis Freitag (Feiertage etc.
	 * werden nicht berücksichtig)
	 * 
	 * @param original
	 *            Ursprünglicher Timestamp
	 * @param werktage
	 *            Anzahl Werktage, die hinzugefügt werden sollen
	 * 
	 * @return Neuer, veränderter Timestamp
	 */
	public static Timestamp addWerktage(final Timestamp original, final int werktage) {

		final Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(original.getTime());

		int werktageNochZuAddieren = werktage;
		while (werktageNochZuAddieren > 0) {
			do {
				cal.add(Calendar.DAY_OF_MONTH, 1);
			} while (!DateUtils.istWerktag(cal));
			werktageNochZuAddieren--;
		}

		final Timestamp result = new Timestamp(cal.getTimeInMillis());
		return result;
	}

}
