package com.example.springbatchsample.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job sampleJob01;

	@RequestMapping("job")
	public String requestJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {

		jobLauncher.run(sampleJob01, createInitialJobParameterMap());
		return "sampleJob";
	}

	private JobParameters createInitialJobParameterMap() {
		Map<String, JobParameter> m = new HashMap<>();
		m.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters p = new JobParameters(m);
		return p;
	}

}