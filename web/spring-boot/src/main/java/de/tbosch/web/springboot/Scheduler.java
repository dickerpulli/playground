package de.tbosch.web.springboot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

	@Scheduled(cron = "0 0/1 * * * *")
	public void minutly() {
		System.out.println("one minute passed");
	}

}
