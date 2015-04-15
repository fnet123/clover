package com.gome.clover.core.scheduler;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.MyJobListener;
import org.quartz.JobKey;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Module Desc:重写 scheduler 实现类
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public enum MyScheduler {
    INSTANCE;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Scheduler scheduler;

    public boolean start() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            if(!scheduler.isStarted()) scheduler.start();
            return true;
        } catch (Exception e) {
            if(logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MyScheduler-->>start() error ", e);
            String execMethod = "MyScheduler-->>start()";
            String execResult = "MyScheduler-->>start() error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    public boolean stop() {
        try {
            if(scheduler.isStarted()) scheduler.shutdown(true);
            return true;
        } catch (SchedulerException e) {
            if(logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MyScheduler-->>stop() error ", e);
            String execMethod = "MyScheduler-->>stop()";
            String execResult = "MyScheduler-->>stop() error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    public boolean add(ClientJob job) {
        try{
            if (isExists(job)){
                if(logger.isDebugEnabled()) System.err.println(" MyScheduler-->>add("+job.toString()+") exists ");
                logger.error("MyScheduler-->>add("+job.toString()+") exists ");
                String execMethod = "MyScheduler-->>add("+job.toString()+")";
                String execResult = "MyScheduler-->>add("+job.toString()+") exists  " ;
                MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                        execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
                return false;
            }
            scheduler.scheduleJob(job.getJobDetail(),job.getTrigger());
           return true;
        }catch (Exception e){
            if(logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MyScheduler-->>addJob("+ job.toString()+") error ",e);
            String execMethod = "MyScheduler-->>addJob("+ job.toString()+") ";
            String execResult = "MyScheduler-->>addJob("+ job.toString()+") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    public boolean addJobListener(MyJobListener listener, ClientJob job) {
        Matcher<JobKey> matcher = KeyMatcher.keyEquals(job.getJobDetail().getKey());
        try {
            scheduler.getListenerManager().addJobListener(listener, matcher);
            return true;
        } catch (SchedulerException e) {
            if(logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MyScheduler-->>addJobListener("+ JSON.toJSONString(listener)+","+job.toString()+") error ",e);
            String execMethod = "MyScheduler-->>addJobListener("+ JSON.toJSONString(listener)+","+job.toString()+") ";
            String execResult = "MyScheduler-->>addJobListener("+ JSON.toJSONString(listener)+","+job.toString()+") error," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    public boolean delete(ClientJob clientJob) {
        try {
            if(scheduler.checkExists(clientJob.getJobDetail().getKey())){
                return scheduler.deleteJob(clientJob.getJobDetail().getKey());
            }else {
                logger.error("MyScheduler-->>delete("+clientJob.toString()+") the clientJob not exist ");
                String execMethod = "MyScheduler-->>delete("+ clientJob.toString()+") ";
                String execResult = "MyScheduler-->>delete("+ clientJob.toString()+") the clientJob not exist " ;
                MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                        execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
                return false;
            }
        } catch (Exception e) {
            if(logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MyScheduler-->>delete("+clientJob.toString()+") error ",e);
            String execMethod = "MyScheduler-->>delete("+clientJob.toString()+")";
            String execResult = "MyScheduler-->>delete("+clientJob.toString()+") error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    public boolean update(ClientJob job) {
        try{
            return delete(job) && add(job);
        }catch (Exception e){
            if(logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MyScheduler-->>update("+ job.toString()+") error ",e);
            String execMethod = "MyScheduler-->>update("+ job.toString()+")  ";
            String execResult = "MyScheduler-->>update("+ job.toString()+") error, " + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    public boolean isExists(ClientJob job) {
        try {
            return scheduler.checkExists(job.getJobDetail().getKey());
        } catch (SchedulerException e) {
            if(logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MyScheduler-->>isExists("+job.toString()+") error ",e);
            String execMethod ="MyScheduler-->>isExists("+job.toString()+")";
            String execResult = "MyScheduler-->>isExists("+job.toString()+") error,"+ e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }

    public boolean isExists(JobKey jobKey) {
        try {
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            if(logger.isDebugEnabled()) e.printStackTrace();
            logger.error("MyScheduler-->>isExists("+ jobKey.toString() +") error ",e);
            String execMethod = "MyScheduler-->>isExists("+ jobKey.toString() +") ";
            String execResult = "MyScheduler-->>isExists("+ jobKey.toString() +") error,"+ e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            return false;
        }
    }
}
