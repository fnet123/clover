package com.gome.clover.core.job;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.netty.client.ObjectReqClient;
import com.gome.clover.common.netty.server.ObjectRespServer;
import com.gome.clover.common.tools.*;
import com.gome.clover.common.zeromq.AsyncSendMsg;
import com.gome.clover.common.zeromq.ZeroMQEntity;
import com.gome.clover.common.zk.ClientDict;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.apache.commons.codec.binary.Base64;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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
 * Module Desc:Server Job
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class ServerJob extends ClientJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ServerJob.class);
    private static final long serialVersionUID = -5273842746335434168L;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ServerJob serverJob = (ServerJob) ClassUtil.BytesToObject(Base64.decodeBase64(
                (String) context.getJobDetail().getJobDataMap().get(CommonConstants.SERVER_JOB_INFO)));
        serverJob.getJobDetail().getJobDataMap().put(CommonConstants.SERVER_JOB_INFO, context.getJobDetail()
                .getJobDataMap().get(CommonConstants.SERVER_JOB_INFO));
        serverJob.getJobDetail().getJobDataMap().put(CommonConstants.CLIENT_JOB_INFO, context.getJobDetail()
                .getJobDataMap().get(CommonConstants.CLIENT_JOB_INFO));
        handlerJob(serverJob);
        deleteJobFromDB(context, serverJob);
    }

    private boolean handlerJob(ServerJob clientJob) {
        String[] fixedServerIps = clientJob.getFixedServerIps();
        if (fixedServerIps != null && fixedServerIps.length > 0) {
            return isContainLocalIp(fixedServerIps) && handlerJobType(clientJob);
        } else {
            return handlerJobType(clientJob);
        }
    }

    private boolean handlerJobType(ServerJob serverJob) {
        switch (serverJob.getJobType()) {
            case REMOTE:
                return handlerRemoteJob(serverJob);
            case BROADCAST:
                return handlerBroadcastJob(serverJob);
            default:
                logger.error("ServerJob-->>handlerJobType(" + serverJob.toString() + ") NOT LOCAL|REMOTE, jobType is " + serverJob.getJobType());
                return false;
        }
    }

    private boolean handlerRemoteJob(ServerJob serverJob) {
        try {
            String[] fixedClientIps = serverJob.getFixedClientIps();
            BasicDBObject clientInfo;
            String clientJobKey = serverJob.getJobDetail().getKey().toString();
            Map retMap;
            if (fixedClientIps != null && fixedClientIps.length > 0) {
                retMap = ClientDict.self.hashClient4SByFixedClientIps(serverJob.getJobClassName(), fixedClientIps);
                clientInfo = (BasicDBObject) retMap.get(CommonConstants.CLIENT_JOB_INFO);
            } else {
                switch (serverJob.getJobStrategy()) {
                    case HASH:
                        retMap = ClientDict.self.hashClient4S(serverJob.getJobClassName(), clientJobKey);
                        break;
                    case SYSTEM_CAPACITY:
                        retMap = ClientDict.self.hashClient4SBySystemCapacity(serverJob.getJobClassName());
                        break;
                    default:
                        retMap = ClientDict.self.hashClient4SBySystemCapacity(serverJob.getJobClassName());
                }
                clientInfo = (BasicDBObject) retMap.get(CommonConstants.CLIENT_JOB_INFO);
            }
            if ((Boolean)retMap.get(CommonConstants.SUCCESS) && null != clientInfo) {
                String clientIp = (String) clientInfo.get(CommonConstants.IP);
               /**
                /*发送zero mq消息队列方式 开始
                String clientPort = (String) clientInfo.get(CommonConstants.PORT);
                AsyncSendMsg.sendWithThreadPool(clientIp, clientPort, JSON.toJSONString(new ZeroMQEntity(CommonConstants.MODULE_TYPE_CLIENT,
                        clientIp, Base64.encodeBase64String(ClassUtil.ObjectToBytes(serverJob)))));
                发送zero mq消息队列方式 结束 **/
                logger.error("************************"+ClassUtil.ObjectToBytes(serverJob).length +"************************");
                logger.error("************************"+Base64.encodeBase64String(ClassUtil.ObjectToBytes(serverJob)).length() +"************************");
                new ObjectReqClient().connect(CommonConstants.NETTY_SERVER_PORT, clientIp, new ZeroMQEntity(CommonConstants.MODULE_TYPE_CLIENT,
                        clientIp, Base64.encodeBase64String(ClassUtil.ObjectToBytes(serverJob))));

            } else {
                //情况很严重 需要报警来解决问题
                DBObject queryCondition = new BasicDBObject();
                queryCondition.put(DBTableInfo.COL_JOB_KEY, clientJobKey);
                queryCondition.put(DBTableInfo.COL_JOB_TYPE, JobType.REMOTE.name());
                DBCollection dbCollection = MongoDBUtil.getCollection(DBTableInfo.TBL_CLOVER_JOB);
                DBObject retObj = dbCollection.findOne(queryCondition);
                String errorCode = (String) retMap.get(CommonConstants.ERROR_CODE);
                String msg = null;
                if (CommonConstants.ERROR_CODE_101.equals(errorCode)) {
                    msg = "ServerJob-->>handlerRemoteJob(" + serverJob.toString() + ")  clientInfo null";
                } else if (CommonConstants.ERROR_CODE_102.equals(errorCode)) {
                    msg = "ServerJob-->>handlerRemoteJob(" + serverJob.toString() + ") the memRatio("
                            + retMap.get(CommonConstants.MEM_RATIO) + ") over max mem ratio("+CommonConstants.MAX_MEM_RATIO+")" +
                            " or the cpuRatio("+retMap.get(CommonConstants.CPU_RATIO)+") over max cpu ratio("+CommonConstants.MAX_CPU_RATIO+") ";
                }
                if (null != retObj && retObj.containsField(DBTableInfo.COL_FAIL_TIMES)) {
                    int failTimes = Integer.parseInt(String.valueOf(retObj.get(DBTableInfo.COL_FAIL_TIMES)));
                    if (failTimes >= 0 && failTimes < CommonConstants.MAX_FAIL_TIMES) {
                        String title = "Client of IP:" + serverJob.getClientIp() + " is error ";
                        SendMailUtil.sendDefaultMail(title, msg);
                        SendMailUtil.sendMailByJobKey(clientJobKey, title, msg);

                        BasicDBObject updateCondition = new BasicDBObject();
                        updateCondition.put(DBTableInfo.COL_FAIL_TIMES, (Integer.parseInt(String.valueOf(retObj.get(DBTableInfo.COL_FAIL_TIMES))) + 1));
                        dbCollection.update(new BasicDBObject(DBTableInfo.COL_ID, retObj.get(DBTableInfo.COL_ID)),
                                new BasicDBObject("$set", updateCondition), true, false);
                    }
                }
                logger.error(msg);
                return false;
            }
        } catch (Exception e) {
            logger.error("ServerJob-->>handlerRemoteJob(" + serverJob.toString() + ") error ", e);
            String execMethod = "ModuleSchedulerServer-->>handlerRemoteJob(" + serverJob.toString() + ")";
            String execResult = "ModuleSchedulerServer-->>handlerRemoteJob(" + serverJob.toString() + ") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
        return false;
    }

    private boolean isContainLocalIp(String ips[]) {
        for (String ip : ips)
            if (ip.equals(IpUtil.getLocalIP()))
                return true;
        return false;
    }

    private void deleteJobFromDB(JobExecutionContext context, ServerJob serverJob) {
        String jobKey = context.getJobDetail().getKey().toString();
        if (context.getNextFireTime() != null) {
            //更新下DB中job状态
            if (MongoDBUtil.isEnabledDB()) {
                BasicDBObject document = getUpdateRemoteJobBasicDBObject(serverJob, jobKey);
                MongoDBUtil.INSTANCE.update(document, DBTableInfo.TBL_CLOVER_JOB);
            }
            return;
        }
        BasicDBObject deleteCondition = new BasicDBObject();
        deleteCondition.put(DBTableInfo.COL_JOB_KEY, jobKey);
        deleteCondition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_REMOTE);
        MongoDBUtil.INSTANCE.delete(deleteCondition, DBTableInfo.TBL_CLOVER_JOB);
        logger.info("ServerJob-->>deleteJobFromDB(" + jobKey + ") remove job [{}] from DB", jobKey);
    }

    private BasicDBObject getUpdateRemoteJobBasicDBObject(ServerJob serverJob, String jobKey) {
        BasicDBObject document = new BasicDBObject();
        document.put(DBTableInfo.COL_JOB_KEY, jobKey);
        document.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_REMOTE);
        document.put(DBTableInfo.COL_EXECUTE_TYPE, serverJob.getExecuteType().name());
        document.put(DBTableInfo.COL_JOB_STRATEGY, serverJob.getJobStrategy().name());
        document.put(DBTableInfo.COL_START_TIME, null != serverJob.getStartTime() ? serverJob.getStartTime() : "");
        document.put(DBTableInfo.COL_CRON_EXPRESSION, null != serverJob.getCronExpression() ? serverJob.getCronExpression() : "");
        document.put(DBTableInfo.COL_JOB_INFO, Base64.encodeBase64String(ClassUtil.ObjectToBytes(serverJob)));
        document.put(DBTableInfo.COL_CLIENT_IP, serverJob.getClientIp());
        document.put(DBTableInfo.COL_SERVER_IP, IpUtil.getLocalIP());
        document.put(DBTableInfo.COL_EXECUTE_START_TIME, "");
        document.put(DBTableInfo.COL_EXECUTE_END_TIME, "");
        document.put(DBTableInfo.COL_FIXED_CLIENT_IPS, null != serverJob.getFixedClientIps() ? JSON.toJSONString(serverJob.getFixedClientIps()) : "");
        document.put(DBTableInfo.COL_FIXED_SERVER_IPS, null != serverJob.getFixedServerIps() ? JSON.toJSONString(serverJob.getFixedServerIps()) : "");
        document.put(DBTableInfo.COL_STATUS, CommonConstants.JOB_STATUS_1);
        document.put(DBTableInfo.COL_TIMES, 0);
        document.put(DBTableInfo.COL_FAIL_TIMES, 0);
        document.put(DBTableInfo.COL_TS, DateUtil.currentDateTime());
        return document;
    }

    private boolean handlerBroadcastJob(ServerJob serverJob) { //该功能慢慢考虑如何实现
        return false;
    }


}
