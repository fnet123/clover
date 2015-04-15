package com.gome.bg.clover.client.job;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Module Desc:Remote Job
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/2
 * Time: 10:33
 */
public abstract class RemoteJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(RemoteJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            executeJob(context);
        } catch (Exception e) {
            logger.error("RemoteJob-->>execute(" + JSON.toJSONString(context) + ") error", e);
            String execMethod = "RemoteJob-->>execute(" + JSON.toJSONString(context) + ")";
            String execResult = "RemoteJob-->>execute(" + JSON.toJSONString(context) + ") error," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
        }

    }


    public abstract void executeJob(JobExecutionContext context);
}
