package de.tbosch.commons.utils;

public class ThreadUtils {

	private ThreadUtils() {
		// utils
	}

	/**
	 * Läßt den aktuellen Thread eine gewisse Zeit schlafen. Verwendet 'Thread.sleep'. InterruptedException wird ggf. in
	 * RuntimeException gewrappet
	 * 
	 * @param sleeptimeInMs
	 *            Schlafzeit in Millisekunden
	 */
	public static void sleep(long sleeptimeInMs) {
		if (sleeptimeInMs >= 0L) {
			try {
				Thread.sleep(sleeptimeInMs);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
