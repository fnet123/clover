package com.gome.clover.common.annotation;

import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.module.ModuleSchedulerClient;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

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
 * Module Desc:Clover Job Annotation Bean
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/28
 * Time: 15:09
 */
@Component
public class CloverJobAnnotationBean implements BeanPostProcessor, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(CloverJobAnnotationBean.class);
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        parseBean(bean);
        return bean;
    }

    private void parseBean(Object bean) {
        CloverJobAnnotation annotation = AnnotationUtils.findAnnotation(bean.getClass(), CloverJobAnnotation.class);
        if (null != annotation) {
            String jobGroup = annotation.jobGroup();
            String jobName = annotation.jobName();
            if (StringUtil.isEmpty(jobName))
                throw new RuntimeException("CloverJobAnnotationBean-->>postProcessAfterInitialization jobName is null");
            ClientJob.JobType jobType = annotation.jobType();
            if (StringUtil.isEmpty(jobType.name())) jobType = ClientJob.JobType.LOCAL;
            ClientJob.ExecuteType executeType = annotation.executeType();
            if (StringUtil.isEmpty(executeType.name())) executeType = ClientJob.ExecuteType.ADD;
            ClientJob.JobStrategy jobStrategy = annotation.jobStrategy();
            if (StringUtil.isEmpty(jobStrategy.name())) jobStrategy = ClientJob.JobStrategy.HASH;
            /**
             * startTime & cronExpression都不为空，那么优先是startTime生效
             */
            String startTime = annotation.startTime();
            String cronExpression = annotation.cronExpression();
            ClientJob clientJob;
            if (!StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(cronExpression)) {
                clientJob = ClientJobBuilder.quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndStartDate(jobGroup, jobName, (Class<? extends Job>) bean.getClass(),
                        jobType, executeType, jobStrategy, DateUtil.formatWithDefaultPattern(startTime));
            } else if (!StringUtil.isEmpty(startTime) && StringUtil.isEmpty(cronExpression)) {
                clientJob = ClientJobBuilder.quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndStartDate(jobGroup, jobName, (Class<? extends Job>) bean.getClass(),
                        jobType, executeType, jobStrategy, DateUtil.formatWithDefaultPattern(startTime));

            } else if (StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(cronExpression)) {
                clientJob = ClientJobBuilder.quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndCronExpression(jobGroup, jobName, (Class<? extends Job>) bean.getClass(),
                        jobType, executeType, jobStrategy, cronExpression);
            } else {
                throw new RuntimeException("CloverJobAnnotationBean-->>postProcessAfterInitialization startTime &  cronExpression  can not null at the same time");
            }
            String fixedClientIps = annotation.fixedClientIps();
            if (!StringUtil.isEmpty(fixedClientIps) && !fixedClientIps.contains("，")) {
                clientJob.setFixedClientIps(fixedClientIps.split(","));
            } else if (!StringUtil.isEmpty(fixedClientIps) && fixedClientIps.contains("，")) {
                logger.error("CloverJobAnnotationBean-->>fixedClientIps can not include chinese comma");
                throw new RuntimeException("CloverJobAnnotationBean-->>fixedClientIps can not include chinese comma");
            }

            String fixedServerIps = annotation.fixedServerIps();
            if (!StringUtil.isEmpty(fixedServerIps) && !fixedServerIps.contains("，")) {
                clientJob.setFixedServerIps(fixedServerIps.split(","));
            } else if (!StringUtil.isEmpty(fixedServerIps) && fixedServerIps.contains("，")) {
                logger.error("CloverJobAnnotationBean-->>fixedServerIps can not include chinese comma");
                throw new RuntimeException("CloverJobAnnotationBean-->>fixedServerIps can not include chinese comma");
            }
            ModuleSchedulerClient.getInstance().handlerJob(clientJob);
        }
    }

}
