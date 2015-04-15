package com.gome.clover.core.job;

import com.gome.clover.core.job.ClientJob.ExecuteType;
import com.gome.clover.core.job.ClientJob.JobStrategy;
import com.gome.clover.core.job.ClientJob.JobType;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;

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
 * Module Desc:Client Job Factory
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */


public class ClientJobFactory {

	public static Builder builder(JobDetail jobDetail, Trigger trigger,String startTime,String cronExpression,String jobClassPath) {
		return new Builder(jobDetail, trigger,startTime,cronExpression,jobClassPath);
	}
	public static Builder builder(JobDetail jobDetail, Trigger trigger, JobType jobType,String startTime,String cronExpression) {
		return new Builder(jobDetail, trigger, jobType,startTime,cronExpression);
	}
	public static Builder builder(JobDetail jobDetail, Trigger trigger, JobType jobType,ExecuteType executeType,
								  String startTime,String cronExpression) {
		return new Builder(jobDetail, trigger, jobType,executeType,startTime,cronExpression);
	}
	public static Builder builder(JobDetail jobDetail, Trigger trigger, JobType jobType,ExecuteType executeType,
								  JobStrategy jobStrategy,String startTime,String cronExpression) {
		return new Builder(jobDetail, trigger, jobType,executeType,jobStrategy,startTime,cronExpression);
	}

	public static class Builder {

		private Trigger trigger;
		private JobDetail jobDetail;
		private ExecuteType executeType;
		private JobType jobType;
		private JobStrategy jobStrategy;
		private String startTime;
		private String cronExpression;
		private String[] fixedClientIps;
		private String[] fixedServerIps;
		private Class<? extends Job> jobClass;
        private String clientIp;
		private String jobClassPath;

		Builder(JobDetail jobDetail, Trigger trigger,String startTime,String cronExpression,String jobClassPath) {
			this.jobDetail = jobDetail;
			this.trigger = trigger;
			this.jobType = JobType.LOCAL;
			this.executeType = ExecuteType.ADD;
			this.jobStrategy = JobStrategy.HASH;
			this.startTime = startTime;
			this.cronExpression = cronExpression;
			this.jobClassPath = jobClassPath;
		}
		Builder(JobDetail jobDetail, Trigger trigger, JobType jobType,String startTime,String cronExpression) {
			this.jobDetail = jobDetail;
			this.trigger = trigger;
			this.jobType = jobType;
			this.executeType = ExecuteType.ADD;
			this.jobStrategy = JobStrategy.HASH;
			this.startTime = startTime;
			this.cronExpression = cronExpression;
		}
		Builder(JobDetail jobDetail, Trigger trigger, JobType jobType,ExecuteType executeType,
				String startTime,String cronExpression) {
			this.jobDetail = jobDetail;
			this.trigger = trigger;
			this.jobType = jobType;
			this.executeType = executeType;
			this.jobStrategy = JobStrategy.HASH;
			this.startTime = startTime;
			this.cronExpression = cronExpression;
		}

		Builder(JobDetail jobDetail, Trigger trigger, JobType jobType,ExecuteType executeType,JobStrategy jobStrategy,
				String startTime,String cronExpression) {
			this.jobDetail = jobDetail;
			this.trigger = trigger;
			this.jobType = jobType;
			this.executeType = executeType;
			this.jobStrategy = jobStrategy;
			this.startTime = startTime;
			this.cronExpression = cronExpression;
		}

		public Builder jobClass(Class<? extends Job> jobClass) {
			this.jobClass = jobClass;
			return this;
		}

		public Builder executeType(ExecuteType executeType) {
			this.executeType = executeType;
			return this;
		}
		public Builder jobStrategy(JobStrategy jobStrategy) {
			this.jobStrategy = jobStrategy;
			return this;
		}

		public Builder startTime(String startTime) {
			this.startTime = startTime;
			return this;
		}

		public Builder cronExpression(String cronExpression) {
			this.cronExpression = cronExpression;
			return this;
		}

		public Builder fixedClientIps(String... fixedClientIps) {
			this.fixedClientIps = fixedClientIps;
			return this;
		}

		public Builder fixedServerIps(String... fixedServerIps) {
			this.fixedServerIps = fixedServerIps;
			return this;
		}

        public Builder clientIp(String ip) {
            this.clientIp = ip;
            return this;
        }

        public ClientJob build() {
			if (jobType != JobType.LOCAL  && jobClass == null)
				throw new ExceptionInInitializerError("ClientJobFactory-->>build() jobClass can not null");
			return new ClientJob(this);
		}

		public Trigger getTrigger() {
			return trigger;
		}

		public JobDetail getJobDetail() {
			return jobDetail;
		}

		public ExecuteType getExecuteType() {
			return executeType;
		}

		public JobType getJobType() {
			return jobType;
		}

		public JobStrategy getJobStrategy() {
			return jobStrategy;
		}

		public String getStartTime() {
			return startTime;
		}

		public String getCronExpression() {
			return cronExpression;
		}

		public String[] getFixedClientIps() {
			return fixedClientIps;
		}

        public String[] getFixedServerIps() {
            return fixedServerIps;
        }

        public Class<? extends Job> getJobClass() {
			return jobClass;
		}

        public String getClientIp() {
            return clientIp;
        }

    }

}
