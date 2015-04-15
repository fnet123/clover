package com.gome.clover.scheduler;

import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobFactory;
import com.gome.clover.core.module.ModuleSchedulerClient;
import com.mongodb.BasicDBObject;
import org.apache.commons.codec.binary.Base64;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Module Desc:
 * User: wangyue-ds6
 * Date: 2014/11/11
 * Time: 22:54
 */
public class TestSchedulerWithInsertMongoDB implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.err.println("TestScheduler-->>execute("+ context+")");
    }

    public static void main(String args[]){
        try {
            //启动Netty RPC
            //new RpcServer(CommonConstants.RPC_CLIENT_PORT).run();
            //ConsumerUtil.startup("10.58.50.204:9876;10.58.50.205:9876", CommonConstants.TOPIC_CLOVER_SERVER, IpUtil.getLocalIP().replace(".", "-"));
        } catch (Exception e) {

        }
        ModuleSchedulerClient client = ModuleSchedulerClient.getInstance();
        JobDetail jobDetail = newJob(TestSchedulerWithInsertMongoDB.class)
                .withIdentity("TestSchedulerJob1", Scheduler.DEFAULT_GROUP).requestRecovery(true).build();
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", Scheduler.DEFAULT_GROUP)
                .withSchedule(cronSchedule("0/10 * * * * ?")).build();
        ClientJob clientJob =  ClientJobFactory.builder(jobDetail, trigger, ClientJob.JobType.REMOTE,null,null).jobClass(TestSchedulerWithInsertMongoDB.class).build();
        String jobInfoStr = Base64.encodeBase64String(ClassUtil.ObjectToBytes(clientJob));
        BasicDBObject document = new BasicDBObject();
        document.put(DBTableInfo.COL_JOB_KEY, "jobKey1");
        document.put(DBTableInfo.COL_JOB_INFO, jobInfoStr);
        document.put(DBTableInfo.COL_IP, IpUtil.getLocalIP());
        document.put(DBTableInfo.COL_STATUS, CommonConstants.JOB_STATUS_1);
        document.put(DBTableInfo.COL_TS, DateUtil.currentDateTime());
        MongoDBUtil.INSTANCE.insertOrUpdate(document,DBTableInfo.TBL_CLOVER_JOB);
    }
}
