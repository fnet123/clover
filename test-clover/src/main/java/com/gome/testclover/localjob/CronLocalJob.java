package com.gome.testclover.localjob;

import com.gome.clover.client.job.LocalJob;
import org.quartz.JobExecutionContext;

import com.gome.clover.common.tools.DateUtils;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.module.ModuleSchedulerClient;

public class CronLocalJob extends LocalJob {

	@Override
	public void executeJob(JobExecutionContext arg0) {
		// TODO Auto-generated method stub
		System.err.println("CronLocalJob--->>>executeJob(JobExecutionContext) @"+ DateUtils.currentDateTime());

	}
	public static void main(String[] args) {
		String jobGroup ="CronLocalJobGroup1";
		String  jobName="CronLocalJobName1";
		String cronExpression = "0/10 * * * * ?";
		ClientJob clientJob = ClientJobBuilder.quickBuildLocalCronJob(jobGroup, jobName, CronLocalJob.class,cronExpression);
		ModuleSchedulerClient.getInstance().startupForLocalJobTest();
		clientJob.setRecoverJobFromDB(true);
		ModuleSchedulerClient.getInstance().handlerJob(clientJob);
		
	}

}
