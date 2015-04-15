package com.gome.clover.core.job;

import com.gome.clover.common.tools.DateUtil;
import org.quartz.Job;

import java.util.Date;

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
 * Module Desc:Client Job Builder
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/1
 * Time: 14:52
 */
public class ClientJobBuilder {

    /**
     * 根据Cron Expression快速创建LOCAL类别job
     *
     * @param jobGroup       job 组
     * @param jobName        job 名称
     * @param jobClass       自定义实现Job接口的
     * @param cronExpression cron表达式
     * @return ClientJob
     */
    public static ClientJob quickBuildLocalCronJob(String jobGroup, String jobName,
                                                   Class<? extends Job> jobClass,
                                                   String cronExpression) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(cronSchedule(cronExpression)).build()), ClientJob.JobType.LOCAL, "", cronExpression).jobClass(jobClass)
                .executeType(ClientJob.ExecuteType.ADD).build();
    }

    /**
     * 根据Cron Expression快速创建REMOTE类别job
     *
     * @param jobGroup       job 组
     * @param jobName        job 名称
     * @param jobClass       自定义实现Job接口的类
     * @param cronExpression cron表达式
     * @return ClientJob
     */
    public static ClientJob quickBuildRemoteCronJob(String jobGroup, String jobName,
                                                    Class<? extends Job> jobClass,
                                                    String cronExpression) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(cronSchedule(cronExpression)).build()), ClientJob.JobType.REMOTE, "", cronExpression).jobClass(jobClass)
                .executeType(ClientJob.ExecuteType.ADD).build();
    }

    /**
     * 根据job开始时间快速创建LOCAL类别job
     *
     * @param jobGroup  job 组
     * @param jobName   job 名称
     * @param jobClass  自定义实现Job接口的类
     * @param startTime 开始执行时间
     * @return
     */
    public static ClientJob quickBuildLocalJobWithStartDate(String jobGroup, String jobName,
                                                            Class<? extends Job> jobClass,
                                                            Date startTime) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .startAt(startTime)).build(), ClientJob.JobType.LOCAL, DateUtil.formatWithDefaultPattern(startTime), "")
                .jobClass(jobClass).executeType(ClientJob.ExecuteType.ADD).build();
    }

    /**
     * 根据job开始时间快速创建REMOTE类别job
     *
     * @param jobGroup  job 组
     * @param jobName   job 名称
     * @param jobClass  自定义实现Job接口的类
     * @param startTime 开始执行时间
     * @return
     */
    public static ClientJob quickBuildRemoteJobWithStartDate(String jobGroup, String jobName,
                                                             Class<? extends Job> jobClass,
                                                             Date startTime) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .startAt(startTime)).build(), ClientJob.JobType.REMOTE, DateUtil.formatWithDefaultPattern(startTime), "")
                .jobClass(jobClass).executeType(ClientJob.ExecuteType.ADD).build();
    }

    /**
     * 根据job开始时间快速创建LOCAL类别job
     *
     * @param jobGroup    job 组
     * @param jobName     job 名称
     * @param jobClass    自定义实现Job接口的类
     * @param executeType ADD,UPDATE,DELETE
     * @return
     */
    public static ClientJob quickBuildLocalStartNowJobWithExecuteType(String jobGroup, String jobName,
                                                                      Class<? extends Job> jobClass,
                                                                      ClientJob.ExecuteType executeType) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .startNow()).build(), ClientJob.JobType.LOCAL, DateUtil.formatWithDefaultPattern(new Date()), "")
                .jobClass(jobClass).executeType(executeType).build();
    }

    /**
     * 根据job开始时间快速创建REMOTE类别job
     *
     * @param jobGroup    job 组
     * @param jobName     job 名称
     * @param jobClass    自定义实现Job接口的类
     * @param executeType ADD,UPDATE,DELETE
     * @return
     */
    public static ClientJob quickBuildRemoteStartNowJobWithExecuteType(String jobGroup, String jobName,
                                                                       Class<? extends Job> jobClass,
                                                                       ClientJob.ExecuteType executeType) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .startNow()).build(), ClientJob.JobType.REMOTE, DateUtil.formatWithDefaultPattern(new Date()), "")
                .jobClass(jobClass).executeType(executeType).build();
    }

    /**
     * 根据job类型&执行类型&执行时间 快速创建job
     *
     * @param jobGroup    job 组
     * @param jobName     job 名称
     * @param jobClass    自定义实现Job接口的类
     * @param jobType     LOCAL,REMOTE
     * @param executeType ADD,UPDATE,DELETE
     * @param startTime   job 执行时间
     * @return ClientJob
     */
    public static ClientJob quickBuildJobWithJobTypeAndExecuteTypeAndStartDate(String jobGroup, String jobName,
                                                                               Class<? extends Job> jobClass,
                                                                               ClientJob.JobType jobType,
                                                                               ClientJob.ExecuteType executeType,
                                                                               Date startTime) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .startAt(startTime)).build(), jobType, executeType, DateUtil.formatWithDefaultPattern(startTime), "")
                .jobClass(jobClass).build();
    }

    /**
     * 根据job类型&执行类型&job策略&执行时间 快速创建job
     *
     * @param jobGroup    job 组
     * @param jobName     job 名称
     * @param jobClass    自定义实现Job接口的类
     * @param jobType     LOCAL,REMOTE
     * @param executeType ADD,UPDATE,DELETE
     * @param jobStrategy HASH,SYSTEM_CAPACITY
     * @param startTime   job 执行时间
     * @return ClientJob
     */
    public static ClientJob quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndStartDate(String jobGroup, String jobName,
                                                                               Class<? extends Job> jobClass,
                                                                               ClientJob.JobType jobType,
                                                                               ClientJob.ExecuteType executeType,
                                                                               ClientJob.JobStrategy jobStrategy,
                                                                               Date startTime) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .startAt(startTime)).build(), jobType, executeType,jobStrategy, DateUtil.formatWithDefaultPattern(startTime), "")
                .jobClass(jobClass).build();
    }

    /**
     * 根据job类型&执行类型&cron执行表达式 快速创建job
     *
     * @param jobGroup       job 组
     * @param jobName        job 名称
     * @param jobClass       自定义实现Job接口的类
     * @param jobType        LOCAL,REMOTE
     * @param executeType    ADD,UPDATE,DELETE
     * @param cronExpression job cron执行表达式
     * @return ClientJob
     */
    public static ClientJob quickBuildJobWithJobTypeAndExecuteTypeAndCronExpression(String jobGroup, String jobName,
                                                                                    Class<? extends Job> jobClass,
                                                                                    ClientJob.JobType jobType,
                                                                                    ClientJob.ExecuteType executeType,
                                                                                    String cronExpression) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(cronSchedule(cronExpression)).build()), jobType, executeType, "", cronExpression)
                .jobClass(jobClass).build();
    }

    /**
     * 根据job类型&执行类型&job策略&cron执行表达式 快速创建job
     *
     * @param jobGroup       job 组
     * @param jobName        job 名称
     * @param jobClass       自定义实现Job接口的类
     * @param jobType        LOCAL,REMOTE
     * @param executeType    ADD,UPDATE,DELETE
     * @param jobStrategy    HASH,SYSTEM_CAPACITY
     * @param cronExpression job cron执行表达式
     * @return ClientJob
     */
    public static ClientJob quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndCronExpression(String jobGroup, String jobName,
                                                                                    Class<? extends Job> jobClass,
                                                                                    ClientJob.JobType jobType,
                                                                                    ClientJob.ExecuteType executeType,
                                                                                    ClientJob.JobStrategy jobStrategy,
                                                                                    String cronExpression) {
        return ClientJobFactory.builder((newJob(jobClass)
                .withIdentity(jobName, jobGroup).build()), (newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(cronSchedule(cronExpression)).build()), jobType, executeType,jobStrategy, "", cronExpression)
                .jobClass(jobClass).build();
    }



}
