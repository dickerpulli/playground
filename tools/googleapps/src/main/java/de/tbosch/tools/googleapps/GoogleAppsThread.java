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
		if (LOG.isInfoEnabled()) {
			LOG.info("running thread");
		}
		trayiconController.registerTrayIcon();
		while (!isInterrupted()) {
			try {
				sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}
		if (LOG.isInfoEnabled()) {
			LOG.info("exiting thread");
		}
	}
}
