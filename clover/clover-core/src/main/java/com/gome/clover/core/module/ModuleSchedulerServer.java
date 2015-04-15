package com.gome.clover.core.module;

import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.netty.server.ObjectRespServer;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.common.tools.PropertiesUtil;
import com.gome.clover.common.tools.StringUtil;

import com.gome.clover.common.zk.ZKManager;
import com.gome.clover.core.job.ServerJob;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
 * Module Desc:Module Scheduler Server
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class ModuleSchedulerServer extends AbstractModuleScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ModuleSchedulerServer.class);
    private volatile static ModuleSchedulerServer moduleSchedulerServer;

    private ModuleSchedulerServer() {
    }

    public static ModuleSchedulerServer getInstance() {
        synchronized (ModuleSchedulerClient.class) {
            if (null == moduleSchedulerServer) {
                moduleSchedulerServer = new ModuleSchedulerServer();
            }
        }
        return moduleSchedulerServer;
    }

    public boolean handlerJob(ServerJob serverJob) {
        if (serverJob == null)
            return false;
        serverJob.setServerIp(IpUtil.getLocalIP());
        logger.error("handlerJob >>> getExecuteType :::"+serverJob.getExecuteType());
        switch (serverJob.getExecuteType()) {
            case ADD:
                return addJob(serverJob);
            case DELETE:
                return deleteJob(serverJob);
            case UPDATE:
                return updateJob(serverJob);
            default:
                return false;
        }
    }

    private boolean addJob(ServerJob serverJob) {
        logger.error("addJob -> MongoDBUtil: insertOrUpdate  >>>");
        try {
            if (!scheduler.add(serverJob)) return false;
            return MongoDBUtil.INSTANCE.insertOrUpdate(BuildMongoDBData.getInsertJobBasicDBObject(serverJob),
                    DBTableInfo.TBL_CLOVER_JOB);
        } catch (Exception e) {
            logger.error("ModuleSchedulerServer-->>addJob(" + serverJob.toString() + ") error", e);
            String execMethod = "ModuleSchedulerServer-->>addJob(" + serverJob.toString() + ")";
            String execResult = "ModuleSchedulerServer-->>addJob(" + serverJob.toString() + ") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject(serverJob.getJobDetail().getKey().toString(),
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    private boolean deleteJob(ServerJob serverJob) {
        logger.error("deleteJob -> MongoDBUtil: delete  >>>");
        try {
            if (!scheduler.delete(serverJob)) return false;
            MongoDBUtil.INSTANCE.delete(getDeleteRemoteJobBasicDBObject(serverJob), DBTableInfo.TBL_CLOVER_JOB);
            return true;
        } catch (Exception e) {
            logger.error("ModuleSchedulerServer-->>deleteJob(" + serverJob.toString() + ") error", e);
            String execMethod = "ModuleSchedulerServer-->>deleteJob(" + serverJob.toString() + ")";
            String execResult = "ModuleSchedulerServer-->>deleteJob(" + serverJob.toString() + ") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject(serverJob.getJobDetail().getKey().toString(),
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    private BasicDBObject getDeleteRemoteJobBasicDBObject(ServerJob deleteJob) {
        BasicDBObject condition = new BasicDBObject();
        condition.put(DBTableInfo.COL_JOB_KEY, deleteJob.getJobDetail().getKey().toString());
        condition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_REMOTE);
        return condition;
    }

    private boolean updateJob(ServerJob serverJob) {
        logger.error("updateJob -> MongoDBUtil: update  >>>");
        try {
            return deleteJob(serverJob) && addJob(serverJob);
        } catch (Exception e) {
            logger.error("ModuleSchedulerServer-->>updateJob(" + serverJob.toString() + ") error", e);
            String execMethod = "ModuleSchedulerServer-->>updateJob(" + serverJob.toString() + ")";
            String execResult = "ModuleSchedulerServer-->>updateJob(" + serverJob.toString() + ") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject(serverJob.getJobDetail().getKey().toString(),
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    public boolean startup() {
        try {
            scheduler.start(); //start scheduler
            String serverIp = IpUtil.getLocalIP();
            registerToZK(CommonConstants.MODULE_TYPE_SERVER,serverIp , null, CommonConstants.ZMQ_SERVER_PORT, CommonConstants.SYSTEM_ID_CLOVER);
            reloadJobFromDB(CommonConstants.MODULE_TYPE_SERVER,serverIp);//从DB中恢复已注册的REMOTE任务
            //resetJobExecuteTime(null,serverIp);
            //AsyncStartZeroMQ.startup(CommonConstants.ZMQ_SERVER_PORT);
            //new ObjectRespServer().bind(CommonConstants.NETTY_SERVER_PORT);
            new ObjectRespServer().start(CommonConstants.NETTY_SERVER_PORT);
            //ServerHeartBeat.INSTNACE.startup();
            return true;
        } catch (Exception e) {
            logger.error("ModuleSchedulerServer-->>start() error", e);
            return false;
        }
    }

    /**
     * 启动 Server端服务
     *
     * @param isRegisterToZK    是否注册到ZK(默认开启)
     * @param isStartupMQ       是否启动MQ(默认开启)
     * @param isReloadJobFromDB 是否从DB中ReloadJob(默认开启)
     * @param systemId          System ID(默认clover)
     * @return
     */
    public boolean startup(boolean isRegisterToZK, boolean isStartupMQ,boolean isStartupNetty, boolean isReloadJobFromDB, String systemId) {
        try {
            scheduler.start(); //start scheduler
            String serverIp = IpUtil.getLocalIP();
            if (isRegisterToZK) {
                registerToZK(CommonConstants.MODULE_TYPE_SERVER, serverIp, null, CommonConstants.ZMQ_SERVER_PORT,
                        systemId);
            }
            if (isStartupNetty) {
               // new ObjectRespServer().bind(CommonConstants.NETTY_SERVER_PORT);
                 new ObjectRespServer().start(CommonConstants.NETTY_SERVER_PORT);
            }
            if (isStartupMQ) {
                //AsyncStartZeroMQ.startup(CommonConstants.ZMQ_SERVER_PORT);
            }
            if (isReloadJobFromDB) {
                reloadJobFromDB(CommonConstants.MODULE_TYPE_SERVER, serverIp);//从DB中恢复已注册的REMOTE任务
            }
            //ServerHeartBeat.INSTNACE.startup();
            //resetJobExecuteTime(null,serverIp);
            return true;
        } catch (Exception e) {
            logger.error("ModuleSchedulerServer-->>start() error", e);
            return false;
        }
    }
    public boolean stop() {
        scheduler.stop();
        try {
            ZKManager zkManager = new ZKManager(PropertiesUtil.loadProperties());
            String serverPathStr = CommonConstants.ZK_ROOT_PATH + "/server";
            List<String> serverNodeList  = zkManager.getZooKeeper().getChildren(serverPathStr,false);
            for (int i = 0; (serverNodeList != null) && (i < serverNodeList.size()); i++) {
                String id = serverNodeList.get(i);
                String c = zkManager.getData(serverPathStr + "/" + id);
                if (c == null) {
                    continue;
                }
                BasicDBObject record = (BasicDBObject) com.mongodb.util.JSON.parse(c);
                String ip = (String) record.get(CommonConstants.IP);
                if(!StringUtil.isEmpty(ip) && IpUtil.getLocalIP().equals(ip)){
                    zkManager.delete(serverPathStr + "/" + id);
                }
            }
            zkManager.close();
        } catch (Exception e) {
            if(logger.isDebugEnabled())e.printStackTrace();
            logger.error("ModuleSchedulerServer-->>stop() error ", e);
        }
        return true;
    }
}
