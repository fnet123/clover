package com.gome.testclover.spring;

import com.gome.bg.clover.client.job.RemoteJob;
import com.gome.clover.common.tools.DateUtil;
import org.quartz.JobExecutionContext;

public class RemoteFixedDateJobWithSpring extends RemoteJob {

	@Override
	public void executeJob(JobExecutionContext arg0) {
		System.err.println("RemoteFixedDateJobWithSpring--->>>executeJob(JobExecutionContext) @"+ DateUtil.currentDateTime());

	}

}
