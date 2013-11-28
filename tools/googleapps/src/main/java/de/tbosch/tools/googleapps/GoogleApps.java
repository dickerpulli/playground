package de.tbosch.tools.googleapps;

import com.sun.javafx.application.PlatformImpl;

import de.tbosch.tools.googleapps.utils.GoogleAppsContext;

/**
 * The main class.
 * 
 * @author thomas.bosch
 */
public class GoogleApps {

	public static void main(String[] args) {
		GoogleAppsContext.load();
		GoogleAppsThread googleAppsThread = (GoogleAppsThread) GoogleAppsContext.getBean("googleAppsThread");
		googleAppsThread.start();
		try {
			googleAppsThread.join();
		} catch (InterruptedException e) {
			// Close will be done in finally block
		} finally {
			PlatformImpl.exit();
			GoogleAppsContext.close();
		}
	}
}
