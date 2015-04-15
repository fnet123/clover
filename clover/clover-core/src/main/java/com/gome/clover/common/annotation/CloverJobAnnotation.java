package com.gome.clover.common.annotation;

import com.gome.clover.core.job.ClientJob;
import org.quartz.JobKey;

import java.lang.annotation.*;

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
 * Module Desc:Clover Job Annotation
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/28
 * Time: 15:09
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CloverJobAnnotation {

    /**
     * job 所属组
     * @return
     */
    String jobGroup() default JobKey.DEFAULT_GROUP;
    /**
     * job 名称
     * @return
     */
    String jobName() default "";

    /**
     * job类型 LOCAL,REMOTE
     * @return
     */
    ClientJob.JobType jobType() default  ClientJob.JobType.LOCAL;

    /**
     * job 执行类型 ADD,UPDATE,DELETE
     * @return
     */
    ClientJob.ExecuteType executeType() default  ClientJob.ExecuteType.ADD;
    /**
     * job 执行策略 HASH,SYSTEM_CAPACITY
     * @return
     */
    ClientJob.JobStrategy jobStrategy() default  ClientJob.JobStrategy.HASH;

    /**
     * job 执行时间
     * @return
     */
    String startTime() default "";

    /**
     * job cron执行表达式
     * @return
     */
    String cronExpression() default "";

    /**
     * 指定客户端IP地址
     * @return
     */
    String fixedClientIps() default "";
    /**
     * 指定服务端IP地址
     * @return
     */
    String fixedServerIps() default "";
}

