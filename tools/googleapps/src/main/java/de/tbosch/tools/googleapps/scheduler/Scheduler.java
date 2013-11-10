package de.tbosch.tools.googleapps.scheduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.tbosch.tools.googleapps.service.GoogleAppsService;

/**
 * A scheduler class to run different timer tasks
 * @author Thomas Bosch
 */
@Component
public class Scheduler {

	private static final Log LOG = LogFactory.getLog(Scheduler.class);

	@Autowired
	private GoogleAppsService googleAppsService;

	@Scheduled(fixedDelay = 300000)
	public void fiveMinutes() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Every-5-minutes-timer is fired");
		}
		try {
			if (googleAppsService.isConnected()) {
				googleAppsService.updateCalendar();
			}
		}
		catch (Exception e) {
			throw new IllegalStateException("Exception while updating calendar", e);
		}
	}

}
