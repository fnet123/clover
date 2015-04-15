package com.gome.clover.job;

import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobFactory;
import com.gome.clover.core.module.ModuleSchedulerClient;
import com.mongodb.BasicDBObject;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


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
 * Module Desc:My Job
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.err.println("context");
        if (null == context.getNextFireTime()) {
            BasicDBObject deleteCondition = new BasicDBObject();
            deleteCondition.put(DBTableInfo.COL_JOB_KEY, context.getJobDetail().getKey().toString());
            deleteCondition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_LOCAL);
            deleteCondition.put(DBTableInfo.COL_IP, IpUtil.getLocalIP());
            MongoDBUtil.INSTANCE.delete(deleteCondition, DBTableInfo.TBL_CLOVER_JOB);
        }
    }

    public static void main(String args[]){

        ModuleSchedulerClient client = ModuleSchedulerClient.getInstance();


        JobDetail jobDetail = newJob(MyJob.class)
                .withIdentity("MyJob", Scheduler.DEFAULT_GROUP).build();
        Trigger trigger = newTrigger()
                .withIdentity("trigger", Scheduler.DEFAULT_GROUP)
                .withSchedule(cronSchedule("0/10 * * * * ?")).build();
        ClientJob clientJob =  ClientJobFactory.builder(jobDetail, trigger, ClientJob.JobType.REMOTE,null,null).jobClass(MyJob.class)
                .executeType(ClientJob.ExecuteType.ADD).build();
        client.handlerJob(clientJob);

    }
}
