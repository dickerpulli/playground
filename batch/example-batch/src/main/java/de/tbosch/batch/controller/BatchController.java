package de.tbosch.batch.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BatchController {

	@Autowired
	private JobOperator jobOperator;

	@RequestMapping(value = "/job/{jobName}/run", method = RequestMethod.POST)
	public @ResponseBody
	String start(@ModelAttribute JobParameters jobParameters, @PathVariable String jobName) {
		try {
			return jobOperator.start(jobName, jobParameters.toString()).toString();
		} catch (JobParametersInvalidException e) {
			return "JobParameters are invalid";
		} catch (NoSuchJobException e) {
			return "Job with name " + jobName + " not found";
		} catch (JobInstanceAlreadyExistsException e) {
			return "JobInstance for job " + jobName + " with parameters " + jobParameters + " already exists";
		}
	}

	@RequestMapping(value = "/job/{jobName}/status", method = RequestMethod.GET)
	public @ResponseBody
	String status(@PathVariable String jobName) {
		try {
			Set<Long> ids = jobOperator.getRunningExecutions(jobName);
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
			return "Job with name '" + jobName + "' is not running";
		}
	}

	@RequestMapping(value = "/job/{jobName}/history", method = RequestMethod.GET)
	public @ResponseBody
	String history(@PathVariable String jobName) {
		try {
			List<Long> instanceIds = jobOperator.getJobInstances(jobName, 0, Integer.MAX_VALUE);
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
			return "Job with name '" + jobName + "' has not been started yet";
		}
	}

	private static class Summary {

		private final List<KeyValue> attributes = new ArrayList<>();

		public static Summary parse(String text) {
			Summary summary = new Summary();
			// see toString() method of JobExecution
			String regex = "(.*): id=(.*), version=(.*), startTime=(.*), endTime=(.*), lastUpdated=(.*), status=(.*), exitStatus=(.*), job=\\[(.*)\\], jobParameters=\\[(.*)\\]";
			Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
			Matcher matcher = pattern.matcher(text);
			if (matcher.find()) {
				summary.attributes.add(new DefaultKeyValue("className", matcher.group(1)));
				summary.attributes.add(new DefaultKeyValue("id", matcher.group(2)));
				summary.attributes.add(new DefaultKeyValue("version", matcher.group(3)));
				summary.attributes.add(new DefaultKeyValue("startTime", matcher.group(4)));
				summary.attributes.add(new DefaultKeyValue("endTime", matcher.group(5)));
				summary.attributes.add(new DefaultKeyValue("lastUpdated", matcher.group(6)));
				summary.attributes.add(new DefaultKeyValue("status", matcher.group(7)));
				summary.attributes.add(new DefaultKeyValue("exitStatus", matcher.group(8)));
				summary.attributes.add(new DefaultKeyValue("job", matcher.group(9)));
				summary.attributes.add(new DefaultKeyValue("jobParameters", matcher.group(10)));
			} else {
				throw new IllegalStateException("Pattern is not valid - see JobExecution toString() method");
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
