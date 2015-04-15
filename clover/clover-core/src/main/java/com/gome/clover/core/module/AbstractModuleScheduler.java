package com.gome.clover.core.module;

import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.systeminfo.SystemInfoUtil;
import com.gome.clover.common.tools.*;
import com.gome.clover.common.zk.ClientDict;
import com.gome.clover.common.zk.ServerDict;
import com.gome.clover.common.zk.ZKUtil;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.scheduler.MyScheduler;
import com.mongodb.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
 * Module Desc:Abstract Module Scheduler
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public abstract class AbstractModuleScheduler {
    private Logger logger = LoggerFactory.getLogger(AbstractModuleScheduler.class);
    protected static MyScheduler scheduler = MyScheduler.INSTANCE;

    public void registerToZK(String moduleType, String ip, List<String> jobClassList, String port, String systemId) {
        try {
            if (CommonConstants.MODULE_TYPE_SERVER.equals(moduleType)) {//服务端
                String serverId = IpUtil.getLocalName() + "@" + ip + "@" + systemId + "@" + DateUtil.currentDateTime();
                BasicDBObject serverNode = new BasicDBObject();
                serverNode.append(CommonConstants.ID, serverId);
                serverNode.append(CommonConstants.PORT, port);
                serverNode.append(CommonConstants.IP, ip);
                serverNode.append(CommonConstants.TS, DateUtil.currentDateTime());
                serverNode.append(CommonConstants.MEM_RATIO,SystemInfoUtil.getMemRatio());
                serverNode.append(CommonConstants.CPU_RATIO,SystemInfoUtil.getCpuRatio());
                serverNode.append(CommonConstants.TOTAL_THREAD,SystemInfoUtil.getTotalThread());
                ZKUtil.setPath(ClientDict.self.getZK(), CommonConstants.ZK_ROOT_PATH + "/" + moduleType + "/" + serverId, serverNode.toString(), CreateMode.EPHEMERAL);
            } else if (CommonConstants.MODULE_TYPE_CLIENT.equals(moduleType)) {//客户端
                if ((jobClassList == null) || (jobClassList.size() == 0)) {
                    return;
                }
                BasicDBList jobClass = new BasicDBList();
                jobClass.addAll(jobClassList);
                String clientId = IpUtil.getLocalName() + "@" + ip + "@" + systemId + "@"+ DateUtil.currentDateTime();
                BasicDBObject clientNode = new BasicDBObject();
                clientNode.append(CommonConstants.ID, clientId);
                clientNode.append(CommonConstants.JOB_CLASS, jobClass);
                clientNode.append(CommonConstants.PORT, port);
                clientNode.append(CommonConstants.IP, ip);
                clientNode.append(CommonConstants.TS, DateUtil.currentDateTime());
                clientNode.append(CommonConstants.MEM_RATIO,SystemInfoUtil.getMemRatio());
                clientNode.append(CommonConstants.CPU_RATIO,SystemInfoUtil.getCpuRatio());
                clientNode.append(CommonConstants.TOTAL_THREAD,SystemInfoUtil.getTotalThread());
                ZKUtil.setPath(ServerDict.self.getZK(), CommonConstants.ZK_ROOT_PATH + "/" + moduleType + "/" + clientId,
                        clientNode.toString(), CreateMode.EPHEMERAL);
            }

        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("AbstractModuleScheduler-->>registerToZK(" + moduleType + "," + ip + ")", e);
            String execMethod = "AbstractModuleScheduler-->>registerToZK(" + moduleType + "," + ip + ")";
            String execResult = "AbstractModuleScheduler-->>registerToZK(" + moduleType + "," + ip + ") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
        }
    }

    public void reloadJobFromDB(String moduleType, String ip) {
        if (CommonConstants.MODULE_TYPE_SERVER.equals(moduleType)) {//服务端
            try {
                DBCollection dbCollection = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_JOB);
                DBObject condition = new BasicDBObject();
                if (!StringUtil.isEmpty(ip)) condition.put(DBTableInfo.COL_SERVER_IP, ip);
                condition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_REMOTE);
                DBCursor cursorDocMap = dbCollection.find(condition);
                while (cursorDocMap.hasNext()) {
                    ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64((String) cursorDocMap.next()
                            .get(DBTableInfo.COL_JOB_INFO)));
                    scheduler.add(clientJob);
                }
                //恢复clover_job_temp表中数据
                dbCollection = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_JOB_TEMP);
                cursorDocMap = dbCollection.find();
                while (cursorDocMap.hasNext()) {
                    DBObject tempDBObject = cursorDocMap.next();
                    ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64((String)
                            tempDBObject.get(DBTableInfo.COL_JOB_INFO)));
                    DBObject retObj = MongoDBUtil.INSTANCE.findOneByCondition(BuildMongoDBData.getDBObjectFromClientJob(clientJob),
                            DBTableInfo.TBL_CLOVER_JOB);
                    if (null == retObj) {
                        scheduler.add(clientJob);
                        MongoDBUtil.INSTANCE.insertOrUpdate(BuildMongoDBData.getInsertJobBasicDBObject(clientJob),
                                DBTableInfo.TBL_CLOVER_JOB);
                        dbCollection.remove(new BasicDBObject(DBTableInfo.COL_ID, tempDBObject.get(DBTableInfo.COL_ID)));//delete
                    }
                }
            } catch (Exception e) {
                logger.error("ModuleSchedulerServer-->>reloadJobFromDB(" + moduleType + "," + ip + ") error", e);
                String execMethod = "ModuleSchedulerServer-->>reloadJobFromDB(" + moduleType + "," + ip + ")";
                String execResult = "ModuleSchedulerServer-->>reloadJobFromDB(" + moduleType + "," + ip + ") error ," + e.getMessage();
                MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                        execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            }

        } else if (CommonConstants.MODULE_TYPE_CLIENT.equals(moduleType)) {//客户端
            try {
                DBCollection dbCollection = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_JOB);
                DBObject condition = new BasicDBObject();
                condition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_LOCAL);
                condition.put(DBTableInfo.COL_CLIENT_IP, ip);
                DBCursor cursorDocMap = dbCollection.find(condition);
                while (cursorDocMap.hasNext()) {
                    ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64((String) cursorDocMap.next()
                            .get(DBTableInfo.COL_JOB_INFO)));
                    scheduler.add(clientJob);
                }
            } catch (Exception e) {
                logger.error("ModuleSchedulerClient-->>reloadJobFromDB(" + ip + ") error", e);
                String execMethod = "ModuleSchedulerServer-->>reloadJobFromDB(" + ip + ")";
                String execResult = "ModuleSchedulerServer-->>reloadJobFromDB(" + ip + ") error ," + e.getMessage();
                MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                        execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            }
        }
    }

    public void resetJobExecuteTime(String clientIp,String serverIp){
        BasicDBObject queryCondition = new BasicDBObject();
        if(!StringUtil.isEmpty(clientIp)) queryCondition.append(DBTableInfo.COL_CLIENT_IP,IpUtil.getLocalIP());
        if(!StringUtil.isEmpty(serverIp)) queryCondition.append(DBTableInfo.COL_SERVER_IP,IpUtil.getLocalIP());
        BasicDBObject updateCondition = new BasicDBObject();
        updateCondition.append(DBTableInfo.COL_EXECUTE_START_TIME,"");
        updateCondition.append(DBTableInfo.COL_EXECUTE_END_TIME,"");
        MongoDBUtil.update(queryCondition,updateCondition,DBTableInfo.TBL_CLOVER_JOB);
    }

    public void resetFailTimes(String clientIp,String serverIp){
        BasicDBObject queryCondition = new BasicDBObject();
        if(!StringUtil.isEmpty(clientIp)) queryCondition.append(DBTableInfo.COL_CLIENT_IP,IpUtil.getLocalIP());
        if(!StringUtil.isEmpty(serverIp)) queryCondition.append(DBTableInfo.COL_SERVER_IP,IpUtil.getLocalIP());
        queryCondition.append(DBTableInfo.COL_FAIL_TIMES,new BasicDBObject("$gt", 0));
        DBCollection dbCollection = MongoDBUtil.getCollection(DBTableInfo.TBL_CLOVER_JOB);
        DBCursor cursorDocMap = dbCollection.find(queryCondition);
        while (cursorDocMap.hasNext()){
            BasicDBObject updateCondition = new BasicDBObject();
            updateCondition.append(DBTableInfo.COL_FAIL_TIMES,0);
            MongoDBUtil.update(new BasicDBObject(DBTableInfo.COL_ID,cursorDocMap.next().get(DBTableInfo.COL_ID)),updateCondition,DBTableInfo.TBL_CLOVER_JOB);
        }
    }
}