package de.tbosch.batch.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

	@RequestMapping(value = "/job/status", method = RequestMethod.GET)
	public @ResponseBody
	String status() {
		try {
			Set<Long> ids = jobOperator.getRunningExecutions(job.getName());
			String status = "";
			for (Long id : ids) {
				try {
					String text = jobOperator.getSummary(id);
					Summary summary = Summary.parse(text);
					status += summary.getAttribute("status").toString() + "<br/>";
				} catch (NoSuchJobExecutionException e) {
					status += "Job with ID '" + id + "' is not running any more<br/>";
				}
			}
			return status;
		} catch (NoSuchJobException e) {
			return "Job with name '" + job.getName() + "' is not running";
		}
	}

	@RequestMapping(value = "/job/history", method = RequestMethod.GET)
	public @ResponseBody
	String history() {
		try {
			List<Long> instanceIds = jobOperator.getJobInstances(job.getName(), 0, Integer.MAX_VALUE);
			String history = "";
			for (Long instanceId : instanceIds) {
				try {
					List<Long> ids = jobOperator.getExecutions(instanceId);
					for (Long id : ids) {
						try {
							String text = jobOperator.getSummary(id);
							Summary summary = Summary.parse(text);
							history += "Execution ID '" + id + "': " + summary.getAttribute("status").toString()
									+ "<br/>";
						} catch (NoSuchJobExecutionException e) {
							history += "Job with ID '" + id + "' is not running any more<br/>";
						}
					}
				} catch (NoSuchJobInstanceException e1) {
					return history += "Job instance with ID '" + instanceId + "' does not exist<br/>";
				}
			}
			return history;
		} catch (NoSuchJobException e) {
			return "Job with name '" + job.getName() + "' has not been started yet";
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
