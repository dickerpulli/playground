package de.tbosch.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
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

}
