package de.tbosch.tools.googleapps;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.tbosch.tools.googleapps.controller.TrayiconController;

/**
 * The thread running for the tray icon app.
 * 
 * @author thomas.bosch
 */
public class GoogleAppsThread extends Thread {

	private static final Log LOG = LogFactory.getLog(GoogleAppsThread.class);

	@Autowired
	private TrayiconController trayiconController;

	@Override
	public void run() {
		LOG.info("running thread");
		AWTExceptionHandler.registerExceptionHandler();
		trayiconController.registerTrayIcon();
		while (!isInterrupted()) {
			try {
				sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}
		LOG.info("exiting thread");
	}

}
