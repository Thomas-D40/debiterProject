package com.project.debiterProject.service;

import java.util.HashMap;
import java.util.Map;


import org.springframework.batch.core.Entity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DebiterJobScheduler {

	@Qualifier("debiterJob")
	@Autowired
	Job debiterJob;

	@Autowired
	JobLauncher jobLauncher;

	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void debiterJobStarter() {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("currentTime", new JobParameter(System.currentTimeMillis()));

		JobParameters jobParameters = new JobParameters(params);

		try {
			JobExecution jobExecution = jobLauncher.run(debiterJob, jobParameters);
			System.out.println("Job Execution ID = " + (jobExecution).getId());
		} catch (Exception e) {
			System.out.println("Exception while starting job");
		}
	}
}
