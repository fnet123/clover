package com.gome.clover.job;

import com.gome.bg.clover.client.job.SimpleJob;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.module.ModuleSchedulerClient;
import org.quartz.JobExecutionContext;

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

public class MyJobWithSimpleJob extends SimpleJob {

    public void executeJob(JobExecutionContext context)  {
    	   System.err.println("MyJobWithSimpleJob-->>executeJob(context)"+context.getJobDetail().getJobDataMap().get("test"));

    }
    public static void main(String args[]){

        ModuleSchedulerClient client = ModuleSchedulerClient.getInstance();
        client.startupForLocalJobTest();
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
            startDate= parser.parse("2014-12-12 11:18:20");
        } catch (ParseException ex) {
        }
       // ClientJob clientJob  = ClientJobBuilder.quickBuildLocalJobWithStartDate("wyGroup1", "wyJob1", MyJobWithSimpleJob.class, startDate);
        ClientJob clientJob  = ClientJobBuilder.quickBuildLocalCronJob("wyGroup2", "wyJob2", MyJobWithSimpleJob.class, "0/10 * * * * ?");
       // ClientJob clientJob  = ClientJobBuilder.quickBuildLocalCronJob("wyGroup", "wyJob", MyJobWithSimpleJob.class, "0/10 * * * * ?");
        //clientJob.setExecuteType(ClientJob.ExecuteType.ADD);
        //ClientJob clientJob  = ClientJobBuilder.quickBuildLocalStartNowJobWithExecuteType("wyGoup", "wyJob", MyJob1.class, ClientJob.ExecuteType.DELETE);
        clientJob.setRecoverJobFromDB(true);
        client.handlerJob(clientJob);


    }

}
