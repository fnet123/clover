package com.gome.clover.job;

import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.module.ModuleSchedulerClient;
import com.mongodb.BasicDBObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * Module Desc:MyJob
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class MyJob1 implements Job,Serializable {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	   System.err.println("WYMyJob1-->>execute(context)"+context.getJobDetail().getJobDataMap().get("test"));
        if (null == context.getNextFireTime()) {
            BasicDBObject deleteCondition = new BasicDBObject();
            deleteCondition.put(DBTableInfo.COL_JOB_KEY, context.getJobDetail().getKey().toString());
            deleteCondition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_LOCAL);
            deleteCondition.put(DBTableInfo.COL_IP, IpUtil.getLocalIP());
            MongoDBUtil.INSTANCE.delete(deleteCondition, DBTableInfo.TBL_CLOVER_JOB);
        }
    }
    public static void main(String args[]){

        ModuleSchedulerClient client =  ModuleSchedulerClient.getInstance();
        //client.startupForLocalJobTest();
       /* JobDetail jobDetail = newJob(MyJob1.class)
                .withIdentity("MyJob1", Scheduler.DEFAULT_GROUP).build();

        Trigger trigger = newTrigger()
                .withIdentity("trigger", Scheduler.DEFAULT_GROUP)
                .withSchedule(cronSchedule("0/10 * * * * ?")).build();

        ClientJob clientJob =  ClientJobFactory.builder(jobDetail, trigger, ClientJob.JobType.REMOTE).jobClass(MyJob1.class)
                .executeType(ExecuteType.ADD).build();
        clientJob.getJobDetail().getJobDataMap().put("test","MyJob1");
        client.handlerJob(clientJob);*/
        Date startDate = null;
        try {
            DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            startDate= parser.parse("2014-12-01 17:23:20");
        } catch (ParseException ex) {
        }
        ClientJob clientJob  = ClientJobBuilder.quickBuildLocalJobWithStartDate("wyGroup", "wyJob", MyJob1.class, startDate);
        clientJob.setExecuteType(ClientJob.ExecuteType.ADD);
        //ClientJob clientJob  = ClientJobBuilder.quickBuildLocalStartNowJobWithExecuteType("wyGoup", "wyJob", MyJob1.class, ClientJob.ExecuteType.DELETE);
        clientJob.setRecoverJobFromDB(true);
        //clientJob.getJobDetail().get
        client.handlerJob(clientJob);

    }

}
