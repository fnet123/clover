package com.gome.clover.common.netty.server;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.compress.CompressUtil;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.netty.client.ObjectReqClient;
import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.common.zeromq.ZeroMQEntity;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.job.ServerJob;
import com.gome.clover.core.module.ModuleSchedulerClient;
import com.gome.clover.core.module.ModuleSchedulerServer;
import com.gome.clover.core.scheduler.MyScheduler;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.netty.channel.*;
import org.apache.commons.codec.binary.Base64;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;


public class SubReqServerHandel extends SimpleChannelInboundHandler {
	private static final Logger logger = LoggerFactory.getLogger(SubReqServerHandel.class);
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		String msg1 = new String(CompressUtil.uncompress((byte[])msg));
		ZeroMQEntity entity = JSON.parseObject(msg1, ZeroMQEntity.class);
		if(entity != null){
			logger.info("Server接收到Client数据成功，开始处理.....");
			this.serverHandelJob(entity,ctx);
			//ctx.writeAndFlush(CompressUtil.compress(entity.getMsg().getBytes()));
		}
		//super.channelRead(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}

	public void  serverHandelJob(ZeroMQEntity zeroMQEntity,ChannelHandlerContext ctx) throws SchedulerException, ClassNotFoundException {
		logger.error("SubReqServerHandel: MODULE_TYPE >>>"+zeroMQEntity.getDestServer());
		try {
		if (CommonConstants.MODULE_TYPE_SERVER.equals(zeroMQEntity.getDestServer())) {
			System.err.println("--------------MODULE_TYPE_SERVER--------------");
			ServerJob serverJob = (ServerJob) ClassUtil.BytesToObject(Base64.decodeBase64(zeroMQEntity.getJobInfo()));
			ModuleSchedulerServer server = ModuleSchedulerServer.getInstance();
			server.handlerJob(serverJob);
		} else if (CommonConstants.MODULE_TYPE_CLIENT.equals(zeroMQEntity.getDestServer())) {
			System.err.println("--------------MODULE_TYPE_CLIENT--------------");
			ServerJob serverJob = (ServerJob) ClassUtil.BytesToObject(Base64.decodeBase64(zeroMQEntity.getJobInfo()));
			ModuleSchedulerClient client = ModuleSchedulerClient.getInstance();
				/**
				ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64((String) serverJob.getJobDetail().getJobDataMap().get(CommonConstants.CLIENT_JOB_INFO)));
				resetClientJobTriggerAndJobType(clientJob);
				client.handlerJob(clientJob);
				 **/
			zeroMQEntity.setMsg(CommonConstants.SUCCESS);
			zeroMQEntity.setJobInfo(Base64.encodeBase64String(ClassUtil.ObjectToBytes(serverJob)));
			//new ObjectReqClient().connect(CommonConstants.NETTY_SERVER_PORT,zeroMQEntity.getDestIp(), zeroMQEntity);// 只传类路径给client端
			ctx.writeAndFlush(CompressUtil.compress(ClassUtil.ObjectToBytes(zeroMQEntity)));
		} else if (CommonConstants.MODULE_TYPE_SERVER_WITH_ADMIN.equals(zeroMQEntity.getDestServer())) {
			String mqMsg = zeroMQEntity.getMsg();
			if (!StringUtil.isEmpty(mqMsg)) {
				System.err.println("--------------MODULE_TYPE_SERVER_WITH_ADMIN--------------");
				BasicDBObject basicDBObject = (BasicDBObject) com.mongodb.util.JSON.parse(mqMsg);
				String jobGroup = (String) basicDBObject.get("jobGroup");
				String jobName = (String) basicDBObject.get("jobName");
				Scheduler scheduler = new StdSchedulerFactory().getScheduler();
				JobKey jobKey = new JobKey(jobName, jobGroup);
				if (scheduler.checkExists(jobKey)) {
					scheduler.deleteJob(jobKey);
				}
			}
		} else if (CommonConstants.MODULE_TYPE_CLIENT_WITH_ADMIN.equals(zeroMQEntity.getDestServer())) {
			String mqMsg = zeroMQEntity.getMsg();
			if (!StringUtil.isEmpty(mqMsg)) {
				System.err.println("--------------MODULE_TYPE_CLIENT_WITH_ADMIN--------------");
				BasicDBObject basicDBObject = (BasicDBObject) com.mongodb.util.JSON.parse(mqMsg);
				String jobGroup = (String) basicDBObject.get("jobGroup");
				String jobName = (String) basicDBObject.get("jobName");
				String jobClassName = (String) basicDBObject.get("jobClassName");
				Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(jobClassName);
				ClientJob.JobType jobType = ClientJob.JobType.valueOf((String) basicDBObject.get("jobType"));
				ClientJob.ExecuteType executeType = ClientJob.ExecuteType.valueOf((String) basicDBObject.get("executeType"));
				ClientJob.JobStrategy jobStrategy = ClientJob.JobStrategy.valueOf((String) basicDBObject.get("jobStrategy"));
				String startTime = (String) basicDBObject.get("startTime");
				String cronExpression = (String) basicDBObject.get("cronExpression");
				ClientJob clientJob = null;
				if (!StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(cronExpression)) {
					clientJob = ClientJobBuilder.quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndStartDate(jobGroup, jobName, jobClass,
							jobType, executeType, jobStrategy, DateUtil.formatWithDefaultPattern(startTime));
				} else if (!StringUtil.isEmpty(startTime) && StringUtil.isEmpty(cronExpression)) {
					clientJob = ClientJobBuilder.quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndStartDate(jobGroup, jobName, jobClass,
							jobType, executeType, jobStrategy, DateUtil.formatWithDefaultPattern(startTime));
				} else if (StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(cronExpression)) {
					clientJob = ClientJobBuilder.quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndCronExpression(jobGroup, jobName, jobClass,
							jobType, executeType, jobStrategy, cronExpression);
				}
				if (null != clientJob) {
					String fixedClientIps = (String) basicDBObject.get("fixedClientIps");
					if (!StringUtil.isEmpty(fixedClientIps))
						clientJob.setFixedClientIps(fixedClientIps.split(","));
					String fixedServerIps = (String) basicDBObject.get("fixedServerIps");
					if (!StringUtil.isEmpty(fixedServerIps))
						clientJob.setFixedServerIps(fixedServerIps.split(","));
					ModuleSchedulerClient.getInstance().handlerJob(clientJob);
				}
			}
		} else if (CommonConstants.MODULE_TYPE_SERVER_WITH_MONITOR.equals(zeroMQEntity.getDestServer())) {
			String mqMsg = zeroMQEntity.getMsg();
			if (!StringUtil.isEmpty(mqMsg)) {
				System.err.println("--------------MODULE_TYPE_SERVER_WITH_MONITOR--------------");
				BasicDBObject basicDBObject = (BasicDBObject) com.mongodb.util.JSON.parse(mqMsg);
				String action = (String) basicDBObject.get("action");
				String ip = (String) basicDBObject.get("ip");
				if (!StringUtil.isEmpty(action) && !StringUtil.isEmpty(ip)) {
					if ("reloadDB".equals(action)) {//恢复数据
						DBCollection dbCollection = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_JOB);
						DBObject condition = new BasicDBObject();
						condition.put(DBTableInfo.COL_SERVER_IP, ip);
						condition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_REMOTE);
						DBCursor cursorDocMap = dbCollection.find(condition);
						while (cursorDocMap.hasNext()) {
							ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64((String) cursorDocMap.next()
									.get(DBTableInfo.COL_JOB_INFO)));
							MyScheduler scheduler = MyScheduler.INSTANCE;
							scheduler.start();
							scheduler.add(clientJob);
						}
					}
				}
			}
		}
			zeroMQEntity.setMsg(CommonConstants.SUCCESS);
		}catch (Exception e){
			zeroMQEntity.setMsg(CommonConstants.ERROR_CODE);
			logger.info("Server回调Client异常",e);
		}
	}

	private void resetClientJobTriggerAndJobType(ClientJob clientJob) {
		clientJob.setTrigger(TriggerBuilder.newTrigger().startNow().withIdentity(clientJob.getTrigger().getKey()).build());
	    clientJob.setJobType(ClientJob.JobType.REMOTE);
    }



}