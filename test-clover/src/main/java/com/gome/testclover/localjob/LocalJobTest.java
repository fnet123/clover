package com.gome.testclover.localjob;

import java.util.Date;

import org.junit.Test;

import com.gome.clover.common.tools.DateUtils;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.module.ModuleSchedulerClient;

public class LocalJobTest {
	@Test
	public void testFixedTimeLocalJob(){
		String jobGroup ="FixedTimeLocalJobGroup";
		String  jobName="FixedTimeLocalJobName";
		Date startTime= DateUtils.formatWithDefaultPattern("2014-12-09 15:40:20");
		ClientJob clientJob = ClientJobBuilder.quickBuildLocalJobWithStartDate(jobGroup, jobName, FixedTimeLocalJob.class,startTime);
		ModuleSchedulerClient.getInstance().startupForLocalJobTest();
		ModuleSchedulerClient.getInstance().handlerJob(clientJob);
	}
	
public static void main(String[] args) {
	String jobGroup ="FixedTimeLocalJobGroup";
	String  jobName="FixedTimeLocalJobName";
	Date startTime= DateUtils.formatWithDefaultPattern("2014-12-09 15:50:20");
	ClientJob clientJob = ClientJobBuilder.quickBuildLocalJobWithStartDate(jobGroup, jobName, FixedTimeLocalJob.class,startTime);
	ModuleSchedulerClient.getInstance().startupForLocalJobTest();
	ModuleSchedulerClient.getInstance().handlerJob(clientJob);
	
}

}
