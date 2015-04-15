package com.gome.testclover.init;

import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.module.ModuleSchedulerClient;
import com.gome.testclover.localjob.CronLocalJob;
import com.gome.testclover.remotejob.RemoteCronJob;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

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
 * Module Desc:test-clover
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/11
 * Time: 20:26
 */
public class InitAllJobWithServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        String jobGroup1 ="CronLocalJobGroup";
        String  jobName1="CronLocalJobName";
        String cronExpression1 = "0/10 * * * * ?";
        ClientJob clientJob1 = ClientJobBuilder.quickBuildLocalCronJob(jobGroup1, jobName1, CronLocalJob.class,cronExpression1);
        clientJob1.setRecoverJobFromDB(true);//是否恢复数据每次启动容器
        ModuleSchedulerClient.getInstance().handlerJob(clientJob1);

        String jobGroup2 = "RomoteCronJob1Group";
        String  jobName2 = "RomoteCronJobName";
        String cronExpression2 = "0/10 * * * * ?";
        ClientJob clientJob2 = ClientJobBuilder.quickBuildRemoteCronJob(jobGroup2, jobName2, RemoteCronJob.class, cronExpression2);
        ModuleSchedulerClient.getInstance().startupForLocalJobTest();
        ModuleSchedulerClient.getInstance().handlerJob(clientJob2);
    }
}
