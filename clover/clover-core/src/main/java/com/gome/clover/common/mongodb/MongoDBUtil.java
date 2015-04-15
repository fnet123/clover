package com.gome.clover.common.mongodb;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.tools.*;
import com.mongodb.*;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
 * Module Desc:MongoDB 工具类
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public enum MongoDBUtil {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(MongoDBUtil.class);
    private static DB db = null;

    private static void getDB() {
        Properties properties = PropertiesUtil.loadProperties("mongoDBConfig.properties");
        try {
            List<ServerAddress> serverAddressArrayList = new ArrayList<ServerAddress>();
            List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
            String mongoAddressStr = (String) properties.get("mongoAddress");
            if (!StringUtil.isEmpty(mongoAddressStr)) {
                String[] ipStr = mongoAddressStr.split(",");
                for (String ip : ipStr) {
                    String[] addressStr = ip.split(":");
                    ServerAddress address = new ServerAddress(addressStr[0], Integer.parseInt(addressStr[1]));
                    serverAddressArrayList.add(address);//地址
                    MongoCredential credential = MongoCredential.createMongoCRCredential(
                            (String) properties.get("userName"), (String) properties.get("dbName"),
                            ((String) properties.get("password")).toCharArray());
                    credentialsList.add(credential);//用户名和密码
                }
                MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
                builder.connectionsPerHost(Integer.parseInt((String) properties.get("connectionsPerHost")));
                builder.socketTimeout(Integer.parseInt((String) properties.get("socketTimeout")));
                builder.maxWaitTime(Integer.parseInt((String) properties.get("maxWaitTime")));
                builder.connectTimeout(Integer.parseInt((String) properties.get("connectTimeout")));
                builder.socketKeepAlive(Boolean.valueOf((String) properties.get("socketKeepAlive")));
                builder.autoConnectRetry(Boolean.valueOf((String) properties.get("autoConnectRetry")));
                builder.threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt((String) properties.get("threadsAllowedToBlockForConnectionMultiplier")));

                MongoClient client = new MongoClient(serverAddressArrayList, credentialsList, builder.build());
                client.setReadPreference(ReadPreference.secondaryPreferred());
                client.setWriteConcern(new WriteConcern(1, 10000, false, false));
                db = client.getDB((String) properties.get("dbName"));
            }

        } catch (UnknownHostException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MongoDBUtil-->>getDB() error", e);
        }
    }

    public static DBCollection getCollection(String collectionName) {
        try {
            if (db == null) {
                getDB();
            }
            return db.getCollection(collectionName);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MongoDBUtil-->>getCollection(collectionName:" + collectionName + ") error", e);
        }
        return null;
    }

    public static DBObject findOneByCondition(DBObject condition, String collectionName) {
        DBCollection dbCollection = getCollection(collectionName);
        return dbCollection.findOne(condition);
    }

    public static DBCursor findByCondition(DBObject condition, String collectionName) {
        DBCollection dbCollection = getCollection(collectionName);
        return dbCollection.find(condition);
    }

    public static boolean insert(BasicDBObject basicDBObject, String collectionName) {
        DBCollection dbCollection = getCollection(collectionName);
        WriteResult writeResult = dbCollection.insert(basicDBObject);
        return writeResult.getN() > 0;
    }

    public static boolean insertOrUpdate(BasicDBObject basicDBObject, String collectionName) {
        logger.error("MongoDBUtil -> insertOrUpdate >>>");
        DBCollection dbCollection = getCollection(collectionName);
        DBObject queryCondition = new BasicDBObject();
        queryCondition.put(DBTableInfo.COL_JOB_KEY, basicDBObject.get(DBTableInfo.COL_JOB_KEY));
        queryCondition.put(DBTableInfo.COL_JOB_TYPE, basicDBObject.get(DBTableInfo.COL_JOB_TYPE));
        DBObject retObj = dbCollection.findOne(queryCondition);
        boolean flag;
        if (null != retObj) { //只更新时间戳TS和状态
            BasicDBObject updateCondition = new BasicDBObject();
            if (null != retObj.get(DBTableInfo.COL_STATUS) && !retObj.get(DBTableInfo.COL_STATUS).equals(CommonConstants.JOB_STATUS_2)) {
                updateCondition.put(DBTableInfo.COL_STATUS, CommonConstants.JOB_STATUS_2);
            }
            if (null != basicDBObject.get(DBTableInfo.COL_JOB_INFO) &&
                    !retObj.get(DBTableInfo.COL_JOB_INFO).equals(basicDBObject.get(DBTableInfo.COL_JOB_INFO))) {
                updateCondition.put(DBTableInfo.COL_JOB_INFO, basicDBObject.get(DBTableInfo.COL_JOB_INFO));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_CLIENT_IP) &&
                    !retObj.get(DBTableInfo.COL_CLIENT_IP).equals(basicDBObject.get(DBTableInfo.COL_CLIENT_IP))) {
                updateCondition.put(DBTableInfo.COL_CLIENT_IP, basicDBObject.get(DBTableInfo.COL_CLIENT_IP));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_SERVER_IP) &&
                    !retObj.get(DBTableInfo.COL_SERVER_IP).equals(basicDBObject.get(DBTableInfo.COL_SERVER_IP))) {
                updateCondition.put(DBTableInfo.COL_SERVER_IP, basicDBObject.get(DBTableInfo.COL_SERVER_IP));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_START_TIME) &&
                    !retObj.get(DBTableInfo.COL_START_TIME).equals(basicDBObject.get(DBTableInfo.COL_START_TIME))) {
                updateCondition.put(DBTableInfo.COL_START_TIME, basicDBObject.get(DBTableInfo.COL_START_TIME));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_START_TIME) &&
                    !retObj.get(DBTableInfo.COL_START_TIME).equals(basicDBObject.get(DBTableInfo.COL_START_TIME))) {
                updateCondition.put(DBTableInfo.COL_START_TIME, basicDBObject.get(DBTableInfo.COL_START_TIME));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_CRON_EXPRESSION) &&
                    !retObj.get(DBTableInfo.COL_CRON_EXPRESSION).equals(basicDBObject.get(DBTableInfo.COL_CRON_EXPRESSION))) {
                updateCondition.put(DBTableInfo.COL_CRON_EXPRESSION, basicDBObject.get(DBTableInfo.COL_CRON_EXPRESSION));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_FIXED_CLIENT_IPS) &&
                    !retObj.get(DBTableInfo.COL_FIXED_CLIENT_IPS).equals(basicDBObject.get(DBTableInfo.COL_FIXED_CLIENT_IPS))) {
                updateCondition.put(DBTableInfo.COL_FIXED_CLIENT_IPS, basicDBObject.get(DBTableInfo.COL_FIXED_CLIENT_IPS));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_FIXED_SERVER_IPS) &&
                    !retObj.get(DBTableInfo.COL_FIXED_SERVER_IPS).equals(basicDBObject.get(DBTableInfo.COL_FIXED_SERVER_IPS))) {
                updateCondition.put(DBTableInfo.COL_FIXED_SERVER_IPS, basicDBObject.get(DBTableInfo.COL_FIXED_SERVER_IPS));
            }
            updateCondition.put(DBTableInfo.COL_TS, DateUtil.currentDateTime());
            flag = dbCollection.update(new BasicDBObject(DBTableInfo.COL_ID, retObj.get(DBTableInfo.COL_ID)),
                    new BasicDBObject("$set", updateCondition), true, false).getN() > 0;
        } else {
            dbCollection.insert(basicDBObject);
            flag = true;
        }
        return flag;
    }

    public static boolean update(BasicDBObject basicDBObject, String collectionName) {
        DBCollection dbCollection = getCollection(collectionName);
        DBObject queryCondition = new BasicDBObject();
        queryCondition.put(DBTableInfo.COL_JOB_KEY, basicDBObject.get(DBTableInfo.COL_JOB_KEY));
        queryCondition.put(DBTableInfo.COL_JOB_TYPE, basicDBObject.get(DBTableInfo.COL_JOB_TYPE));
        DBObject retObj = dbCollection.findOne(queryCondition);
        boolean flag = false;
        if (null != retObj) { //只更新时间戳TS和状态
            BasicDBObject updateCondition = new BasicDBObject();
            if (null != retObj.get(DBTableInfo.COL_STATUS) && !retObj.get(DBTableInfo.COL_STATUS).equals(CommonConstants.JOB_STATUS_2)) {
                updateCondition.put(DBTableInfo.COL_STATUS, CommonConstants.JOB_STATUS_2);
            }
            if (null != basicDBObject.get(DBTableInfo.COL_EXECUTE_TYPE) &&
                    !retObj.get(DBTableInfo.COL_EXECUTE_TYPE).equals(basicDBObject.get(DBTableInfo.COL_EXECUTE_TYPE))) {
                updateCondition.put(DBTableInfo.COL_EXECUTE_TYPE, basicDBObject.get(DBTableInfo.COL_EXECUTE_TYPE));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_JOB_STRATEGY) &&
                    !retObj.get(DBTableInfo.COL_JOB_STRATEGY).equals(basicDBObject.get(DBTableInfo.COL_JOB_STRATEGY))) {
                updateCondition.put(DBTableInfo.COL_JOB_STRATEGY, basicDBObject.get(DBTableInfo.COL_JOB_STRATEGY));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_JOB_INFO) &&
                    !retObj.get(DBTableInfo.COL_JOB_INFO).equals(basicDBObject.get(DBTableInfo.COL_JOB_INFO))) {
                updateCondition.put(DBTableInfo.COL_JOB_INFO, basicDBObject.get(DBTableInfo.COL_JOB_INFO));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_CLIENT_IP) &&
                    !retObj.get(DBTableInfo.COL_CLIENT_IP).equals(basicDBObject.get(DBTableInfo.COL_CLIENT_IP))) {
                updateCondition.put(DBTableInfo.COL_CLIENT_IP, basicDBObject.get(DBTableInfo.COL_CLIENT_IP));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_SERVER_IP) &&
                    !retObj.get(DBTableInfo.COL_SERVER_IP).equals(basicDBObject.get(DBTableInfo.COL_SERVER_IP))) {
                updateCondition.put(DBTableInfo.COL_SERVER_IP, basicDBObject.get(DBTableInfo.COL_SERVER_IP));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_START_TIME) &&
                    !retObj.get(DBTableInfo.COL_START_TIME).equals(basicDBObject.get(DBTableInfo.COL_START_TIME))) {
                updateCondition.put(DBTableInfo.COL_START_TIME, basicDBObject.get(DBTableInfo.COL_START_TIME));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_START_TIME) &&
                    !retObj.get(DBTableInfo.COL_START_TIME).equals(basicDBObject.get(DBTableInfo.COL_START_TIME))) {
                updateCondition.put(DBTableInfo.COL_START_TIME, basicDBObject.get(DBTableInfo.COL_START_TIME));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_CRON_EXPRESSION) &&
                    !retObj.get(DBTableInfo.COL_CRON_EXPRESSION).equals(basicDBObject.get(DBTableInfo.COL_CRON_EXPRESSION))) {
                updateCondition.put(DBTableInfo.COL_CRON_EXPRESSION, basicDBObject.get(DBTableInfo.COL_CRON_EXPRESSION));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_FIXED_CLIENT_IPS) &&
                    !retObj.get(DBTableInfo.COL_FIXED_CLIENT_IPS).equals(basicDBObject.get(DBTableInfo.COL_FIXED_CLIENT_IPS))) {
                updateCondition.put(DBTableInfo.COL_FIXED_CLIENT_IPS, basicDBObject.get(DBTableInfo.COL_FIXED_CLIENT_IPS));
            }
            if (null != basicDBObject.get(DBTableInfo.COL_FIXED_SERVER_IPS) &&
                    !retObj.get(DBTableInfo.COL_FIXED_SERVER_IPS).equals(basicDBObject.get(DBTableInfo.COL_FIXED_SERVER_IPS))) {
                updateCondition.put(DBTableInfo.COL_FIXED_SERVER_IPS, basicDBObject.get(DBTableInfo.COL_FIXED_SERVER_IPS));
            }
            updateCondition.put(DBTableInfo.COL_TS, DateUtil.currentDateTime());
            flag = dbCollection.update(new BasicDBObject(DBTableInfo.COL_ID, retObj.get(DBTableInfo.COL_ID)),
                    new BasicDBObject("$set", updateCondition), true, false).getN() > 0;
        }
        return flag;
    }

    public static boolean delete(BasicDBObject condition, String collectionName) {
        DBCollection dbCollection = getCollection(collectionName);
        WriteResult writeResult = dbCollection.remove(condition);
        return writeResult.getN() > 0;
    }

    public static boolean update(BasicDBObject queryCondition, BasicDBObject updateCondition, String collectionName) {
        try {
            DBCollection dbCollection = getCollection(collectionName);
            dbCollection.update(queryCondition, new BasicDBObject("$set", updateCondition),true,true);
            return true;

        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MongoDBUtil-->>findAndModify(" + JSON.toJSONString(queryCondition) + "," + JSON.toJSONString(updateCondition) + ")", e);
            return false;
        }
    }

    public static void updateOrDeleteDB(JobExecutionContext context) {
        BasicDBObject condition = new BasicDBObject();
        condition.put(DBTableInfo.COL_JOB_KEY, context.getJobDetail().getKey().toString());
        condition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_LOCAL);
        condition.put(DBTableInfo.COL_CLIENT_IP, IpUtil.getLocalIP());
        DBCollection dbCollection = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_JOB);
        DBObject retObj = dbCollection.findOne(condition);
        boolean flag;
        if (context.getNextFireTime() != null) {
            if (null != retObj) { //只更新时间戳TS和状态
                BasicDBObject updateCondition = new BasicDBObject();
                if (!retObj.get(DBTableInfo.COL_STATUS).equals(CommonConstants.JOB_STATUS_2)) {
                    updateCondition.put(DBTableInfo.COL_STATUS, CommonConstants.JOB_STATUS_2);
                }
                updateCondition.put(DBTableInfo.COL_TS, DateUtil.currentDateTime());
                flag = dbCollection.update(new BasicDBObject(DBTableInfo.COL_ID, retObj.get(DBTableInfo.COL_ID)),
                        new BasicDBObject("$set", updateCondition), true, false).getN() > 0;
                if (!flag) {
                    logger.error("MongoDBUtil-->>updateOrDeleteDB(" + JSON.toJSONString(context) + ") error");
                }
            }
            return;
        }
        if (null == context.getNextFireTime()) {
            if (null != retObj) {
                BasicDBObject deleteCondition = new BasicDBObject();
                deleteCondition.put(DBTableInfo.COL_JOB_KEY, context.getJobDetail().getKey().toString());
                deleteCondition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_LOCAL);
                deleteCondition.put(DBTableInfo.COL_CLIENT_IP, IpUtil.getLocalIP());
                dbCollection.remove(deleteCondition);
            }
        }
    }

    /**
     * 更新job 开始执行时间和结束执行时间
     *
     * @param context
     * @param updateType 1:update executeStartTime;2: update executeEndTime
     */
    public static void updateJobTime(JobExecutionContext context, int updateType) {
        BasicDBObject condition = new BasicDBObject();
        condition.put(DBTableInfo.COL_JOB_KEY, context.getJobDetail().getKey().toString());
        condition.put(DBTableInfo.COL_CLIENT_IP, IpUtil.getLocalIP());
        DBCollection dbCollection = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_JOB);
        DBObject retObj = dbCollection.findOne(condition);
        boolean flag;
        if (null != retObj) {
            if (1 == updateType) {
                BasicDBObject updateCondition = new BasicDBObject();
                updateCondition.put(DBTableInfo.COL_EXECUTE_START_TIME, DateUtil.currentDateTime());
                updateCondition.put(DBTableInfo.COL_EXECUTE_END_TIME, "");
                flag = dbCollection.update(new BasicDBObject(DBTableInfo.COL_ID, retObj.get(DBTableInfo.COL_ID)),
                        new BasicDBObject("$set", updateCondition), true, false).getN() > 0;
                if (!flag) {
                    logger.error("MongoDBUtil-->>updateJobTime(" + JSON.toJSONString(context) + "," + updateType + ") error");
                }
            } else if (2 == updateType) {
                BasicDBObject updateCondition = new BasicDBObject();
                updateCondition.put(DBTableInfo.COL_EXECUTE_END_TIME, DateUtil.currentDateTime());
                updateCondition.put(DBTableInfo.COL_TIMES, (Long.parseLong(String.valueOf(retObj.get(DBTableInfo.COL_TIMES))) + 1));
                flag = dbCollection.update(new BasicDBObject(DBTableInfo.COL_ID, retObj.get(DBTableInfo.COL_ID)),
                        new BasicDBObject("$set", updateCondition), true, false).getN() > 0;
                if (!flag) {
                    logger.error("MongoDBUtil-->>updateJobTime(" + JSON.toJSONString(context) + "," + updateType + ") error");
                }
            }
        }
    }

    public static boolean isEnabledDB(){
        DBObject condition = new BasicDBObject();
        condition.put(DBTableInfo.COL_CONFIG_KEY,CommonConstants.DISABLED_DB);
        condition.put(DBTableInfo.COL_CONFIG_TYPE,CommonConstants.TYPE_DB);
        DBObject  result = MongoDBUtil.INSTANCE.findOneByCondition(condition,DBTableInfo.TBL_CLOVER_CONFIG);
        if(null!=result && result.containsField(DBTableInfo.COL_CONFIG_VALUE)  &&  !Boolean.valueOf((String) result.get(DBTableInfo.COL_CONFIG_VALUE))){
            return false;
        } else {
            return true;
        }
    }
}
