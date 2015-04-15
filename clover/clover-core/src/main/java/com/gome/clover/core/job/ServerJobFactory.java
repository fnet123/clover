package com.gome.clover.core.job;

import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.common.tools.CommonConstants;
import org.apache.commons.codec.binary.Base64;
import org.quartz.*;
import org.quartz.spi.MutableTrigger;

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
 * Module Desc:Server Job Factory
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class ServerJobFactory {
    public static ServerJob createControlJob(ClientJob clientJob) {
        ServerJob serverJob = new ServerJob();
        serverJob.setExecuteType(clientJob.getExecuteType());
        serverJob.setJobStrategy(clientJob.getJobStrategy());
        serverJob.setStartTime(clientJob.getStartTime());
        serverJob.setCronExpression(clientJob.getCronExpression());
        serverJob.setFixedClientIps(clientJob.getFixedClientIps());
        serverJob.setFixedServerIps(clientJob.getFixedServerIps());
        serverJob.setClientIp(clientJob.getClientIp());
        serverJob.setJobType(ClientJob.JobType.REMOTE);
        serverJob.setJobDetail(createJobDetail(clientJob));
        serverJob.setTrigger(createTrigger(clientJob));
        serverJob.setJobClassName(clientJob.getJobClassName());
        serverJob.setRecoverJobFromDB(clientJob.isRecoverJobFromDB());
        serverJob.setJobClassPath(clientJob.getJobClassPath());
        setJobInfo(serverJob, clientJob);
        return serverJob;
    }

    protected static TriggerKey createServerTriggerKey(ClientJob clientJob) {
        return new TriggerKey(getClientJobName(clientJob), getClientJobGroup(clientJob));
       // return new TriggerKey(createServerJobName(clientJob),CommonConstants.REMOTE_JOB_GROUP);
    }

    private static JobKey createServerJobKey(ClientJob clientJob) {
        return new JobKey(getClientJobName(clientJob), getClientJobGroup(clientJob));
        //return new JobKey(createServerJobName(clientJob),CommonConstants.REMOTE_JOB_GROUP);
    }

    private static String createServerJobName(ClientJob clientJob) {
        return getClientJobGroup(clientJob) + "." + getClientJobName(clientJob);
    }

    private static String getClientJobName(ClientJob clientJob) {
        return clientJob.getJobDetail().getKey().getName();
    }

    private static String getClientJobGroup(ClientJob clientJob) {
        return clientJob.getJobDetail().getKey().getGroup();
    }

    private static JobDetail createJobDetail(ClientJob clientJob) {
        return JobBuilder.newJob(ServerJob.class).withIdentity(createServerJobKey(clientJob)).build();
    }

    private static Trigger createTrigger(ClientJob clientJob) {
        Trigger trigger = clientJob.getTrigger();
        if ((trigger instanceof MutableTrigger) == false)
            return null;
        byte[] byteTrigger = ClassUtil.ObjectToBytes(trigger);
        MutableTrigger mutableTrigger = (MutableTrigger) ClassUtil.BytesToObject(byteTrigger);
        mutableTrigger.setKey(createServerTriggerKey(clientJob));
        mutableTrigger.setJobKey(createServerJobKey(clientJob));
        return mutableTrigger;
    }

    private static void setJobInfo(ServerJob serverJob, ClientJob clientJob) {
        serverJob.getJobDetail().getJobDataMap().put(CommonConstants.SERVER_JOB_INFO,Base64.encodeBase64String(
                ClassUtil.ObjectToBytes(serverJob)));
        /**
        serverJob.getJobDetail().getJobDataMap().put(CommonConstants.CLIENT_JOB_INFO,Base64.encodeBase64String(
                ClassUtil.ObjectToBytes(clientJob)));
         **/
        serverJob.getJobDetail().getJobDataMap().put(CommonConstants.CLIENT_JOB_PATH,clientJob.getJobClassName());

    }

}
