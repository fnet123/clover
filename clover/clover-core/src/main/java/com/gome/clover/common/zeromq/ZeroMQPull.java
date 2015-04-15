package com.gome.clover.common.zeromq;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.compress.CompressUtil;
import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.*;
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
import org.apache.commons.codec.binary.Base64;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.zeromq.ZMQ;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

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
 * Module Desc:ZeroMQ Pull
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public enum ZeroMQPull {
    INSTANCE;
    private static ZMQ.Context context;
    private static ZMQ.Socket pull;
    private static boolean isStop = false;

    public void recv(String addr) {
        context = ZMQ.context(1);
        pull = context.socket(ZMQ.PULL);
        pull.bind(addr);
        System.err.println("ZeroMQPull-->> recv(" + addr + ") init.....");
        while (!isStop) {
            try {
                String msg = new String(CompressUtil.uncompress(pull.recv()));
                //System.err.println("ZeroMQPull-->>recv(" + addr + ")" + msg);
                if (!StringUtil.isEmpty(msg)) {
                    ZeroMQEntity zeroMQEntity = JSON.parseObject(msg, ZeroMQEntity.class);
                    if (IpUtil.getLocalIP().equals(zeroMQEntity.getDestIp())) {
                        if (CommonConstants.MODULE_TYPE_SERVER.equals(zeroMQEntity.getDestServer())) {
                            System.err.println("--------------MODULE_TYPE_SERVER--------------");
                            ServerJob serverJob = (ServerJob) ClassUtil.BytesToObject(Base64.decodeBase64(zeroMQEntity.getJobInfo()));
                            ModuleSchedulerServer server = ModuleSchedulerServer.getInstance();
                            server.handlerJob(serverJob);
                        } else if (CommonConstants.MODULE_TYPE_CLIENT.equals(zeroMQEntity.getDestServer())) {
                            System.err.println("--------------MODULE_TYPE_CLIENT--------------");
                            ServerJob serverJob = (ServerJob) ClassUtil.BytesToObject(Base64.decodeBase64(zeroMQEntity.getJobInfo()));
                            ModuleSchedulerClient client = ModuleSchedulerClient.getInstance();
                            ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64(
                                    (String) serverJob.getJobDetail().getJobDataMap().get(CommonConstants.CLIENT_JOB_INFO)));
                            resetClientJobTriggerAndJobType(clientJob);
                            client.handlerJob(clientJob);
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
                    }
                }
            } catch (Exception e) {
                String execMethod = "ZeroMQPull-->>recv(" + addr + ")";
                String execResult = "ZeroMQPull-->>recv(" + addr + ")" + " error ," + e.getMessage();
                MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                        execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            }
        }
    }

    private void resetClientJobTriggerAndJobType(ClientJob clientJob) {
        clientJob.setTrigger(TriggerBuilder.newTrigger().startNow().withIdentity(clientJob.getTrigger().getKey()).build());
        clientJob.setJobType(ClientJob.JobType.LOCAL);
    }

    public boolean stop() {//server 关闭zmq方法
        isStop = true;
        pull.disconnect("tcp://" + IpUtil.getLocalIP() + ":" + CommonConstants.ZMQ_SERVER_PORT);
        pull.close();
        context.close();
        return true;
    }
    public boolean stop(String ip,String port) {
        isStop = true;
        pull.disconnect("tcp://" + ip + ":" + port);
        pull.close();
        context.close();
        return true;
    }

    public static void main(String args[]) {
        String addr = "tcp://*:" + RandomNumUtil.getNextIntString();
        ZeroMQPull.INSTANCE.recv(addr);
    }
}
