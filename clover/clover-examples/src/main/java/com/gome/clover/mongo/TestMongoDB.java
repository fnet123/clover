package com.gome.clover.mongo;

import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.core.job.ClientJob;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

/**
 * Module Desc:Test Mongo DB
 * User: wangyue-ds6
 * Date: 2014/11/18
 * Time: 15:02
 */
public class TestMongoDB {
    @Test
    public void insertOrUpdate(){
        while (true){
            BasicDBObject document = new BasicDBObject();
            document.put("jobKey", "jobKey1");
            document.put("jobInfo", "jobInfo1");
            document.put("ip", IpUtil.getLocalIP());
            document.put("ts", DateUtil.currentDateTime());
            MongoDBUtil.INSTANCE.insertOrUpdate(document, "test_wy");
            MongoDBUtil.INSTANCE.insert(document, "test_wy");
            System.err.println("TestMongoDB--->>>insertOrUpdate()");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    @Test
    public void findByJobKey(){
        DBCollection dbCollection = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_JOB);
        DBObject condition = new BasicDBObject();
        condition.put(DBTableInfo.COL_IP, IpUtil.getLocalIP());
        DBCursor cursorDocMap = dbCollection.find(condition);
        while (cursorDocMap.hasNext()) {
            ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64((String)cursorDocMap.next()
                    .get(DBTableInfo.COL_JOB_INFO)));
            System.err.println("clientJob:"+clientJob.getTrigger());
        }
    }

    @Test
    public void insertConfig(){
        BasicDBObject document = new BasicDBObject();
        document.put(DBTableInfo.COL_CONFIG_KEY,CommonConstants.DISABLED_DB);
        document.put(DBTableInfo.COL_CONFIG_TYPE,CommonConstants.TYPE_DB);
        document.put(DBTableInfo.COL_CONFIG_VALUE, "false");
        document.put("ts", DateUtil.currentDateTime());
        MongoDBUtil.INSTANCE.insert(document,DBTableInfo.TBL_CLOVER_CONFIG);
        System.err.println("TestMongoDB--->>>insertConfig()");
    }
    @Test
    public  void updateConfig(){
        BasicDBObject document = new BasicDBObject();
        document.put(DBTableInfo.COL_CONFIG_KEY,CommonConstants.DISABLED_DB);
        document.put(DBTableInfo.COL_CONFIG_TYPE,CommonConstants.TYPE_DB);
        BasicDBObject update = new BasicDBObject();
        update.put(DBTableInfo.COL_CONFIG_VALUE,"true");
        MongoDBUtil.INSTANCE.update(document,new BasicDBObject("$set", update),DBTableInfo.TBL_CLOVER_CONFIG);
    }
    @Test
    public void testGetOn(){
        System.err.println(""+MongoDBUtil.isEnabledDB());
    }
}
