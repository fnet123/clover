package com.gome.testclover.remotejob;

import com.gome.clover.client.job.RemoteJob;
import com.gome.clover.common.tools.DateUtils;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.module.ModuleSchedulerClient;
import org.quartz.JobExecutionContext;

public class RemoteCronJob extends RemoteJob {

	@Override
	public void executeJob(JobExecutionContext arg0) {
		System.err.println("RemoteCronJob--->>>executeJob(JobExecutionContext) @"+ DateUtils.currentDateTime());

	}
	public static void main(String[] args) {
		String jobGroup ="RemoteCronJob1Group1";
		String  jobName="RemoteCronJobName1";
		String cronExpression = "0/10 * * * * ?";
		ClientJob clientJob = ClientJobBuilder.quickBuildRemoteCronJob(jobGroup, jobName, RemoteCronJob.class,cronExpression);
		ModuleSchedulerClient.getInstance().startupForLocalJobTest();
		//clientJob.setRecoverJobFromDB(true);
		ModuleSchedulerClient.getInstance().handlerJob(clientJob);
	}

}
