package com.gome.clover.core.job;

import com.gome.clover.common.mongodb.MongoDBUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Module Desc:My Job Listener(Listener数据存储到Redis中)
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class MyJobListener implements JobListener {

	private static Logger logger = LoggerFactory.getLogger(MyJobListener.class);

	@Override
	public String getName() {
		return MyJobListener.class.getSimpleName();
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		if(MongoDBUtil.isEnabledDB()){
			MongoDBUtil.updateJobTime(context,1);
		}
	}
	//logger.info("job [{}] is about to be executed.", getJobKey(context).toString());
	//System.err.println("jobToBeExecuted:"+context.getJobDetail().getKey());
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.info("job [{}] execution was vetoed.", getJobKey(context).toString());
		System.err.println("jobExecutionVetoed:"+context.getJobDetail().getKey());
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		//logger.info("job [{}] was executed.", getJobKey(context).toString());
		//System.err.println("jobWasExecuted:"+context.getJobDetail().getKey());
		if(MongoDBUtil.isEnabledDB()){
			MongoDBUtil.updateJobTime(context,2);
		}
	}

	private JobKey getJobKey(JobExecutionContext context) {
		return context.getJobDetail().getKey();
	}
}
