package de.tbosch.batch.test;

import java.util.Collections;

import org.springframework.batch.core.JobParameters;
import org.springframework.web.client.RestTemplate;

public class JobStarter {

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		String id = restTemplate.postForObject("http://localhost:8080/job/retry-job/run", null, String.class,
				Collections.singletonMap("jobParameters", new JobParameters()));
		System.out.println(id);
	}

}
