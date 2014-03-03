package de.tbosch.batch.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BatchController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobOperator jobOperator;

	@Autowired
	private Job job;

	@RequestMapping(value = "/job/run", method = RequestMethod.GET)
	public @ResponseBody
	String start() {
		try {
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
			return jobExecution.getJobId().toString();
		} catch (JobParametersInvalidException e) {
			return "JobParameters are invalid";
		} catch (JobExecutionAlreadyRunningException e) {
			return "Job already running";
		} catch (JobRestartException e) {
			return "Job cannot be restarted";
		} catch (JobInstanceAlreadyCompleteException e) {
			return "JobInstance already completed";
		}
	}

	@RequestMapping(value = "/job/{id}/status", method = RequestMethod.GET)
	public @ResponseBody
	String status(@PathVariable String id) {
		try {
			String text = jobOperator.getSummary(Long.valueOf(id));
			Summary summary = Summary.parse(text);
			return summary.getAttribute("status").toString();
		} catch (NumberFormatException e) {
			return "Job ID '" + id + "'must be a number";
		} catch (NoSuchJobExecutionException e) {
			return "Job with ID '" + id + "' was not found";
		}
	}

	private static class Summary {

		private final List<KeyValue> attributes = new ArrayList<>();

		public static Summary parse(String text) {
			Summary summary = new Summary();
			String[] attrs = text.trim().split(",");
			for (String attr : attrs) {
				String key = attr.trim().split("=")[0];
				String value = attr.trim().split("=")[1];
				summary.attributes.add(new DefaultKeyValue(key, value));
			}
			return summary;
		}

		public Object getAttribute(Object key) {
			for (KeyValue attribute : attributes) {
				if (attribute.getKey().equals(key)) {
					return attribute.getValue();
				}
			}
			throw new IllegalArgumentException("Key '" + key + "' was not found in attributes list");
		}
	}

}
