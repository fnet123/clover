package com.gome.clover.common.mongodb;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ServerJob;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.codec.binary.Base64;
import sun.net.util.IPAddressUtil;

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
 * Module Desc:clover
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/27
 * Time: 20:17
 */
public class BuildMongoDBData {
    public static BasicDBObject getInsertJobBasicDBObject(ClientJob clientJob) {
        BasicDBObject document = new BasicDBObject();
        document.put(DBTableInfo.COL_JOB_KEY, clientJob.getJobDetail().getKey().toString());
        document.put(DBTableInfo.COL_JOB_TYPE, clientJob.getJobType().name());
        document.put(DBTableInfo.COL_EXECUTE_TYPE, clientJob.getExecuteType().name());
        document.put(DBTableInfo.COL_JOB_STRATEGY, clientJob.getJobStrategy().name());
        document.put(DBTableInfo.COL_START_TIME, null != clientJob.getStartTime()?clientJob.getStartTime():"");
        document.put(DBTableInfo.COL_CRON_EXPRESSION,  null != clientJob.getCronExpression()?clientJob.getCronExpression():"");
        document.put(DBTableInfo.COL_JOB_INFO, Base64.encodeBase64String(ClassUtil.ObjectToBytes(clientJob)));
        document.put(DBTableInfo.COL_CLIENT_IP, null != clientJob.getClientIp()?clientJob.getClientIp() : "");
        if(clientJob.getJobType() == ClientJob.JobType.REMOTE) document.put(DBTableInfo.COL_SERVER_IP, null != clientJob.getServerIp()?clientJob.getServerIp() : IpUtil.getLocalIP());
        document.put(DBTableInfo.COL_FIXED_CLIENT_IPS, null != clientJob.getFixedClientIps() ? JSON.toJSONString(clientJob.getFixedClientIps()) : "");
        document.put(DBTableInfo.COL_FIXED_SERVER_IPS, null != clientJob.getFixedServerIps() ? JSON.toJSONString(clientJob.getFixedServerIps()) : "");
        document.put(DBTableInfo.COL_EXECUTE_START_TIME,"");
        document.put(DBTableInfo.COL_EXECUTE_END_TIME, "");
        document.put(DBTableInfo.COL_TIMES,0);
        document.put(DBTableInfo.COL_FAIL_TIMES,0);
        document.put(DBTableInfo.COL_STATUS, CommonConstants.JOB_STATUS_1);
        document.put(DBTableInfo.COL_TS, DateUtil.currentDateTime());
        return document;
    }
    public static BasicDBObject getDeleteJobBasicDBObject(ClientJob clientJob) {
        BasicDBObject condition = new BasicDBObject();
        condition.put(DBTableInfo.COL_JOB_KEY, clientJob.getJobDetail().getKey().toString());
        condition.put(DBTableInfo.COL_JOB_TYPE,clientJob.getJobType().name());
        return condition;
    }

    public static BasicDBObject getInsertLogBasicDBObject(String jobKey, String execMethod, String execResult) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put(DBTableInfo.COL_SYSTEM_ID, CommonConstants.SYSTEM_ID_CLOVER);
        basicDBObject.put(DBTableInfo.COL_IP, IpUtil.getLocalIP());
        basicDBObject.put(DBTableInfo.COL_JOB_KEY, jobKey);
        basicDBObject.put(DBTableInfo.COL_EXEC_METHOD, execMethod);
        basicDBObject.put(DBTableInfo.COL_EXEC_RESULT, execResult);
        basicDBObject.put(DBTableInfo.COL_EXEC_TIME, DateUtil.currentDateTime());
        basicDBObject.put(DBTableInfo.COL_TS, DateUtil.currentDateTime());
        return basicDBObject;
    }

    public static void saveTempJobData2DB(ServerJob serverJob) {
        DBObject queryCondition = new BasicDBObject();
        queryCondition.put(DBTableInfo.COL_JOB_KEY, serverJob.getJobDetail().getKey().toString());
        queryCondition.put(DBTableInfo.COL_CLIENT_IP, serverJob.getClientIp());
        DBObject retObj = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_JOB).findOne(queryCondition);
        if (null == retObj) {
            BasicDBObject document = BuildMongoDBData.getInsertJobBasicDBObject(serverJob);
            MongoDBUtil.INSTANCE.insertOrUpdate(document, DBTableInfo.TBL_CLOVER_JOB_TEMP);
        }
    }

    public static DBObject getDBObjectFromClientJob(ClientJob clientJob){
        DBObject dbObject = new BasicDBObject();
        dbObject.put(DBTableInfo.COL_JOB_KEY, clientJob.getJobDetail().getKey().toString());
        dbObject.put(DBTableInfo.COL_JOB_TYPE, clientJob.getJobType().name());
        return dbObject;
    }

}
