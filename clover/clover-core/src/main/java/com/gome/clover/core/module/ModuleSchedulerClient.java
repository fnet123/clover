package com.gome.clover.core.module;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.netty.client.ObjectReqClient;
import com.gome.clover.common.netty.server.ObjectRespServer;
import com.gome.clover.common.tools.*;

import com.gome.clover.common.zeromq.ZeroMQEntity;
import com.gome.clover.common.zk.ServerDict;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.MyJobListener;
import com.gome.clover.core.job.ServerJob;
import com.gome.clover.core.job.ServerJobFactory;
import com.gome.clover.core.monitor.client.ClientHeartBeat;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
 * Module Desc:Module Scheduler Client
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class ModuleSchedulerClient extends AbstractModuleScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ModuleSchedulerClient.class);
    private volatile static ModuleSchedulerClient moduleSchedulerClient = null;
    private List<ClientJob> defaultJobs = new LinkedList();

    private ModuleSchedulerClient() {
        scheduler.start();
    }

    public static ModuleSchedulerClient getInstance() {
        synchronized (ModuleSchedulerClient.class) {
            if (null == moduleSchedulerClient) {
                moduleSchedulerClient = new ModuleSchedulerClient();
            }
        }
        return moduleSchedulerClient;
    }

    /**
     * 启动 Client端服务
     *
     * @param jobClassList   注册job class到zk(默认启动)
     * @param isRegisterToZK 是否启动注册到zk服务(默认启动)
     * @param isStartupNetty    是否启动Netty服务(默认启动)
     * @param port           MQ服务启动端口号
     * @param token          token验证码
     * @return true:成功;false:失败
     */
    public boolean startup(List<String> jobClassList, boolean isRegisterToZK, boolean isStartupNetty, String port,
                           String systemId, String token) {
        if (!StringUtil.isEmpty(token) && CommonConstants.token.equals(token)) {
            if (StringUtil.isEmpty(systemId))
                throw new RuntimeException("ModuleSchedulerClient-->>startup(" + JSON.toJSONString(jobClassList) + "," + isRegisterToZK + "," + isStartupNetty + ") systemId null ");
            try {
                scheduler.start(); //start scheduler
                String clientIP = IpUtil.getLocalIP();
                if (StringUtil.isEmpty(port) || (!StringUtil.isEmpty(port) && "-1".equals(port)))
                    port =RandomNumUtil.getNextIntString();//String.valueOf(NetUtils.getAvailablePort(1025));
                if (isRegisterToZK) {
                    registerToZK(CommonConstants.MODULE_TYPE_CLIENT, clientIP, jobClassList, port, systemId);
                }
                if (isStartupNetty) {
                   // AsyncStartZeroMQ.startup(port);
                   // new ObjectRespServer().start(CommonConstants.NETTY_SERVER_PORT);//.bind(Integer.valueOf(CommonConstants.NETTY_SERVER_PORT));
                    new ObjectRespServer().bind(CommonConstants.NETTY_SERVER_PORT);
                }
                reloadJobFromDB(CommonConstants.MODULE_TYPE_CLIENT, clientIP);
                addDefaultJob();
                //resetJobExecuteTime(clientIP,null);
                resetFailTimes(clientIP, null);
                ClientHeartBeat.INSTNACE.startup();
                System.err.println("ModuleSchedulerClient-->>startup(" + JSON.toJSONString(jobClassList) + "," + isRegisterToZK + "," + isStartupNetty + ") ....");
                return true;
            } catch (Exception e) {
                logger.error("ModuleSchedulerClient-->>startup(" + JSON.toJSONString(jobClassList) + "," + isRegisterToZK + "," + isStartupNetty + ") error", e);
                String execMethod = "ModuleSchedulerClient-->>startup(" + JSON.toJSONString(jobClassList) + "," + isRegisterToZK + "," + isStartupNetty + ") error";
                String execResult = "ModuleSchedulerClient-->>startup(" + JSON.toJSONString(jobClassList) + "," + isRegisterToZK + "," + isStartupNetty + ") error ," + e.getMessage();
                MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                        execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
                throw new RuntimeException(e);
            }
        } else if (!StringUtil.isEmpty(token) && !CommonConstants.token.equals(token)) {
            logger.error("ModuleSchedulerClient-->>startup(" + JSON.toJSONString(jobClassList) + "," + isRegisterToZK + "," + isStartupNetty + ") token wrong");
            throw new RuntimeException("ModuleSchedulerClient-->>startup(" + JSON.toJSONString(jobClassList) + "," + isRegisterToZK + "," + isStartupNetty + ") token wrong");
        } else if (StringUtil.isEmpty(token)) {
            logger.error("ModuleSchedulerClient-->>startup(" + JSON.toJSONString(jobClassList) + "," + isRegisterToZK + "," + isStartupNetty + ") token null");
            throw new RuntimeException("ModuleSchedulerClient-->>startup(" + JSON.toJSONString(jobClassList) + "," + isRegisterToZK + "," + isStartupNetty + ") token null");
        }
        return false;
    }

    public boolean startupForLocalJobTest() {
        try {
            scheduler.start(); //start scheduler
            return true;
        } catch (Exception e) {
            logger.error("ModuleSchedulerClient-->>startupForLocalJobTest() error", e);
            return false;
        }
    }

    public void addDefaultJob() {
        for (ClientJob clientJob : defaultJobs) {
            handlerJob(clientJob);
        }
    }

    public boolean stop() {
        scheduler.stop();
        return true;
    }

    public boolean handlerJob(ClientJob clientJob) {
        if (clientJob == null)
            return false;
        clientJob.setClientIp(IpUtil.getLocalIP());
        clientJob.setJobClassName(clientJob.getJobClass().getName());
        String[] fixedClientIps = clientJob.getFixedClientIps();
        if (fixedClientIps != null && fixedClientIps.length > 0) {
            return isContainLocalIp(fixedClientIps) && handlerJobType(clientJob);
        } else {
            return handlerJobType(clientJob);
        }
    }

    private boolean handlerJobType(ClientJob clientJob) {
        switch (clientJob.getJobType()) {
            case LOCAL:
                return handlerLocalJob(clientJob);
            case REMOTE:
                return handlerRemoteJob(clientJob);
            default:
                logger.error("ModuleSchedulerClient-->>handlerJobType(" + clientJob.toString() + ") NOT LOCAL|REMOTE, jobType is " + clientJob.getJobType());
                return false;
        }
    }

    private boolean handlerLocalJob(ClientJob clientJob) {
        if (null == clientJob) {
            assert clientJob != null;
            logger.error("ModuleSchedulerClient-->>handlerLocalJob(" + clientJob.toString() + ") clientJob null ");
            return false;
        }
        switch (clientJob.getExecuteType()) {
            case ADD:
                return addLocalJob(clientJob);
            case DELETE:
                return deleteLocalJob(clientJob);
            case UPDATE:
                return updateLocalJob(clientJob);
            default:
                logger.error("ModuleSchedulerClient-->>handlerLocalJob(" + clientJob.toString() + ") NOT ADD|DELETE|UPDATE, executeType is " + clientJob.getExecuteType());
                return false;
        }
    }

    private boolean addLocalJob(ClientJob clientJob) {
        try {
            if (!scheduler.add(clientJob)) return false;
            scheduler.addJobListener(new MyJobListener(), clientJob);
            if (clientJob.isRecoverJobFromDB()) {
                return MongoDBUtil.INSTANCE.insertOrUpdate(BuildMongoDBData.getInsertJobBasicDBObject(clientJob),
                        DBTableInfo.TBL_CLOVER_JOB);
            }
        } catch (Exception e) {
            logger.error("ModuleSchedulerClient-->>addLocalJob(" + clientJob.toString() + ") error", e);
            String execMethod = "ModuleSchedulerClient-->>addLocalJob(" + clientJob.toString() + ")";
            String execResult = "ModuleSchedulerClient-->>addLocalJob(" + clientJob.toString() + ") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
        return true;
    }

    private boolean deleteLocalJob(ClientJob clientJob) {
        try {
            if (!scheduler.delete(clientJob)) return false;
            BasicDBObject condition = BuildMongoDBData.getDeleteJobBasicDBObject(clientJob);
            if (clientJob.isRecoverJobFromDB()) {
                if (!MongoDBUtil.INSTANCE.delete(condition, DBTableInfo.TBL_CLOVER_JOB)) return false;
            }
        } catch (Exception e) {
            logger.error("ModuleSchedulerClient-->>deleteLocalJob(" + clientJob.toString() + ") error", e);
            String execMethod = "ModuleSchedulerClient-->>deleteLocalJob(" + clientJob.toString() + ") error";
            String execResult = "ModuleSchedulerClient-->>deleteLocalJob(" + clientJob.toString() + ") error," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
        return true;
    }

    private boolean updateLocalJob(ClientJob clientJob) {
        try {
            return deleteLocalJob(clientJob) && addLocalJob(clientJob);
        } catch (Exception e) {
            logger.error("ModuleSchedulerClient-->>updateLocalJob(" + clientJob.toString() + ") error", e);
            String execMethod = "ModuleSchedulerClient-->>reloadJobFromDB(" + clientJob.toString() + ")";
            String execResult = "ModuleSchedulerClient-->>reloadJobFromDB(" + clientJob.toString() + ") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    private boolean handlerRemoteJob(ClientJob clientJob) {
        try {
            ServerJob serverJob = ServerJobFactory.createControlJob(clientJob);
            DBObject retObj = MongoDBUtil.INSTANCE.findOneByCondition(BuildMongoDBData.getDBObjectFromClientJob(serverJob),
                    DBTableInfo.TBL_CLOVER_JOB);
            if (null != retObj) return false;
            String[] fixedServerIps = clientJob.getFixedServerIps();
            BasicDBObject serverInfo = null;
            Map retMap = null;
            if (fixedServerIps != null && fixedServerIps.length > 0) {
                retMap = ServerDict.self.hashServer4SByFixedServerIps(fixedServerIps);
                serverInfo = (BasicDBObject) retMap.get(CommonConstants.SERVER_JOB_INFO);
            } else {
                if (CommonConstants.SERVER_JOB_STRATEGY.equalsIgnoreCase(ClientJob.JobStrategy.HASH.name())) {
                    retMap = ServerDict.self.hashServer4S(null);
                    serverInfo = (BasicDBObject) retMap.get(CommonConstants.SERVER_JOB_INFO);
                } else if (CommonConstants.SERVER_JOB_STRATEGY.equalsIgnoreCase(ClientJob.JobStrategy.SYSTEM_CAPACITY.name())) {
                    retMap = ServerDict.self.hashServer4SBySystemCapacity();
                    serverInfo = (BasicDBObject) retMap.get(CommonConstants.SERVER_JOB_INFO);
                }
            }
            if ((Boolean) retMap.get(CommonConstants.SUCCESS) && null != serverInfo) {//双重判断，保证获取的serverInfo是存在的
                String ip = (String) serverInfo.get(CommonConstants.IP);
                int port = CommonConstants.NETTY_SERVER_PORT;
                 /*发送zero mq消息队列方式 开始*/
                // String addr = "tcp://" + ip + ":" + port;
                // ZeroMQEntity zeroMQEntity = new ZeroMQEntity(CommonConstants.MODULE_TYPE_SERVER,ip, Base64.encodeBase64String(ClassUtil.ObjectToBytes(serverJob)));
                //ZeroMQPush.INSTANCE.send(addr, JSON.toJSONString(zeroMQEntity));
                //AsyncSendMsg.sendWithClientThreadPool(ip, port, JSON.toJSONString(zeroMQEntity));
                /*发送zero mq消息队列方式 结束*/
                new ObjectReqClient().connect(port,ip, new ZeroMQEntity(CommonConstants.MODULE_TYPE_SERVER,
                        ip, Base64.encodeBase64String(ClassUtil.ObjectToBytes(serverJob))));   
                return true;
            } else { //报警通知
                String errorCode = (String) retMap.get(CommonConstants.ERROR_CODE);
                String msg = null;
                if (CommonConstants.ERROR_CODE_101.equals(errorCode)) {
                    msg = "ModuleSchedulerClient-->>handlerRemoteJob(" + clientJob.toString() + ")  serverInfo null";
                } else if (CommonConstants.ERROR_CODE_102.equals(errorCode)) {
                    msg = "ModuleSchedulerClient-->>handlerRemoteJob(" + clientJob.toString() + ") the memRatio("
                            + retMap.get(CommonConstants.MEM_RATIO) + ") over max mem ratio  "+
                      " or the cpuRatio("+retMap.get(CommonConstants.CPU_RATIO)+") over max cpu ratio("+CommonConstants.MAX_CPU_RATIO+") ";
                } else if (CommonConstants.ERROR_CODE_102.equals(errorCode)) {
                    msg = "ModuleSchedulerClient-->>handlerRemoteJob(" + clientJob.toString() + ") errorCode is " + errorCode;
                }
                //记录数到MongoDB中，server重启后灾难恢复
                BuildMongoDBData.saveTempJobData2DB(serverJob);
                //情况很严重 需要报警来解决问题
                SendMailUtil.sendDefaultMail(msg, msg);
                logger.error(msg);
                return false;
            }

        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("ModuleSchedulerClient-->>handlerRemoteJob(" + clientJob.toString() + ")", e);
            String execMethod = "ModuleSchedulerClient-->>handlerRemoteJob(" + clientJob.toString() + ")";
            String execResult = "ModuleSchedulerClient-->>handlerRemoteJob(" + clientJob.toString() + ") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }


    private boolean isContainLocalIp(String ips[]) {
        for (String ip : ips)
            if (ip.equals(IpUtil.getLocalIP()))
                return true;
        return false;
    }

    private boolean isContainFixedIp(String ip, String fixedIps[]) {
        for (String fixedIp : fixedIps)
            if (ip.equals(fixedIp))
                return true;
        return false;
    }


    public List<ClientJob> getDefaultJobs() {
        return defaultJobs;
    }

    public void setDefaultJobs(List<ClientJob> defaultJobs) {
        this.defaultJobs = defaultJobs;
    }
}