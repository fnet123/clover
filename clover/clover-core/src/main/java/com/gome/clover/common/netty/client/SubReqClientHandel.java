package com.gome.clover.common.netty.client;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.compress.CompressUtil;
import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.common.zeromq.ZeroMQEntity;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ServerJob;
import com.gome.clover.core.module.ModuleSchedulerClient;
import io.netty.channel.*;
import org.apache.commons.codec.binary.Base64;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SubReqClientHandel  extends SimpleChannelInboundHandler {
	private static final Logger logger = LoggerFactory.getLogger(SubReqClientHandel.class);
	private ZeroMQEntity  entity;
	public SubReqClientHandel(ZeroMQEntity  entity){
		this.entity=entity;
	}
	public SubReqClientHandel(){
	}
	/**
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		logger.error("ChannelHandlerContext.channelActive().write()  >>>>>>>ctx.channel().config():"+ctx.channel().config());
		ctx.write(CompressUtil.compress( JSON.toJSONString(entity).getBytes()));
		ctx.flush();
		logger.error("ChannelHandlerContext.channelActive().write()");
	}
	**/
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("******Client接收到Server回调数据成功******");
		String result = new String(CompressUtil.uncompress((byte[])msg));
		ZeroMQEntity entity = JSON.parseObject(result, ZeroMQEntity.class);
		if (entity!=null){
			ServerJob serverJob = (ServerJob) ClassUtil.BytesToObject(Base64.decodeBase64(entity.getJobInfo()));
			ModuleSchedulerClient client = ModuleSchedulerClient.getInstance();
			ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64((String) serverJob.getJobDetail().getJobDataMap().get(CommonConstants.CLIENT_JOB_INFO)));
			resetClientJobTriggerAndJobType(clientJob);
			client.handlerJob(clientJob);
			logger.info("client接收到服务器返回的消息:" + msg);
		}else {
			logger.error("client accept Server resp Error!");
		}
		ctx.close();
	}
	private void resetClientJobTriggerAndJobType(ClientJob clientJob) {
		clientJob.setTrigger(TriggerBuilder.newTrigger().startNow().withIdentity(clientJob.getTrigger().getKey()).build());
		clientJob.setJobType(ClientJob.JobType.REMOTE);
	}
}
