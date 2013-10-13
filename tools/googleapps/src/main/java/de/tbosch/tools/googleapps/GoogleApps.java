package de.tbosch.tools.googleapps;

import de.tbosch.tools.googleapps.utils.GoogleAppsContext;

/**
 * The main class.
 * 
 * @author thomas.bosch
 */
public class GoogleApps {

	public static void main(String[] args) {
		GoogleAppsContext.load();
		GoogleAppsThread timetoolThread = (GoogleAppsThread) GoogleAppsContext.getBean("googleAppsThread");
		timetoolThread.start();
		try {
			timetoolThread.join();
		} catch (InterruptedException e) {
			// Close will be done in finally block
		} finally {
			GoogleAppsContext.close();
		}
	}
}
