package com.gome.testclover.spring;

import com.gome.bg.clover.client.job.RemoteJob;
import com.gome.clover.common.annotation.CloverJobAnnotation;
import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.core.job.ClientJob;
import org.quartz.JobExecutionContext;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * Module Desc:Log4j ConfigListener
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/25
 * Time: 10:55
 */
//@Component
@CloverJobAnnotation(jobGroup = "RemoteJobWithSpringAnnotationGroup",jobName = "RemoteJobWithSpringAnnotationName",
jobType = ClientJob.JobType.REMOTE,executeType = ClientJob.ExecuteType.ADD,startTime="2014-12-12 12:12:12")
public class RemoteFixedDateJobWithSpringAnnotation extends RemoteJob {
	@Override
	public void executeJob(JobExecutionContext arg0) {
		System.err.println("RemoteJobWithSpringAnnotation--->>>executeJob(JobExecutionContext) @"+ DateUtil.currentDateTime());
	}
}
