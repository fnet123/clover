package com.gome.clover.common.annotation;

import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.module.ModuleSchedulerClient;
import org.quartz.Job;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

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
 * Module Desc:Clover Job Bean Definition Parser
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/3
 * Time: 17:53
 */
public class CloverJobBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final Logger logger = LoggerFactory.getLogger(CloverJobBeanDefinitionParser.class);
    private static final String CLOVER_JOB_ANNOTATION = "CLOVER_JOB_ANNOTATION";

    @Override
    protected Class<?> getBeanClass(Element element) {
        try {
            return Class.forName(element.getAttribute("id"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);
        doParseElement(element);
        if (!parserContext.getRegistry().containsBeanDefinition(CLOVER_JOB_ANNOTATION)) {
            RootBeanDefinition annotation = new RootBeanDefinition(CloverJobAnnotationBean.class);
            parserContext.getRegistry().registerBeanDefinition(CLOVER_JOB_ANNOTATION, annotation);
        }
    }

    /**
     * 解析 spring cloverjob:job标签配置元素，并创建ClientJob(代码一大坨 一大坨的 都不忍直视了)
     *
     * @param element
     */
    private void doParseElement(Element element) {
        //解析 spring cloverjob:config标签配置元素 开始
        String id = element.getAttribute("id");
        String jobGroup = element.getAttribute("jobGroup");
        if (StringUtil.isEmpty(jobGroup)) jobGroup = JobKey.DEFAULT_GROUP;
        String jobName = element.getAttribute("jobName");
        if (StringUtil.isEmpty(jobName)) jobName = id;
        String jobType = element.getAttribute("jobType");

        if (StringUtil.isEmpty(jobType)) {
            jobType = ClientJob.JobType.LOCAL.name();
        } else if (!jobType.equalsIgnoreCase(ClientJob.JobType.LOCAL.name())
                && !jobType.equalsIgnoreCase(ClientJob.JobType.REMOTE.name())) {
            logger.error("CloverJobBeanDefinitionParser-->>doParseElement() jobType must be LOCAL or REMOTE ");
            throw new RuntimeException("CloverJobBeanDefinitionParser-->>doParse() jobType must be LOCAL or REMOTE ");
        }
        String executeType = element.getAttribute("executeType");
        if (StringUtil.isEmpty(executeType)) {
            executeType = ClientJob.ExecuteType.ADD.name();
        } else if (!executeType.equalsIgnoreCase(ClientJob.ExecuteType.ADD.name())
                && !executeType.equalsIgnoreCase(ClientJob.ExecuteType.UPDATE.name())
                && !executeType.equalsIgnoreCase(ClientJob.ExecuteType.DELETE.name())) {
            logger.error("CloverJobBeanDefinitionParser-->>doParseElement() executeType must be ADD or UPDATE or DELETE  ");
            throw new RuntimeException("CloverJobBeanDefinitionParser-->>doParse() executeType must be ADD or UPDATE or DELETE  ");
        }
        String jobStrategy = element.getAttribute("jobStrategy");
        if (StringUtil.isEmpty(jobStrategy)) {
            jobStrategy = ClientJob.JobStrategy.HASH.name();
        } else if (!jobStrategy.equalsIgnoreCase(ClientJob.JobStrategy.HASH.name())
                && !jobStrategy.equalsIgnoreCase(ClientJob.JobStrategy.SYSTEM_CAPACITY.name())) {
            logger.error("CloverJobBeanDefinitionParser-->>doParseElement() jobStrategy must be HASH or SYSTEM_CAPACITY  ");
            throw new RuntimeException("CloverJobBeanDefinitionParser-->>doParse() jobStrategy must be HASH or SYSTEM_CAPACITY ");
        }
        String startTime = element.getAttribute("startTime");
        String cronExpression = element.getAttribute("cronExpression");
        //解析 spring cloverjob:config标签配置元素 结束
        //创建ClientJob 开始
        ClientJob clientJob;
        if (!StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(cronExpression)) {
            try {
                clientJob = ClientJobBuilder.quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndStartDate(jobGroup,
                        jobName,(Class<? extends Job>) Class.forName(id),
                        ClientJob.JobType.valueOf(jobType), ClientJob.ExecuteType.valueOf(executeType),
                        ClientJob.JobStrategy.valueOf(jobStrategy),
                        DateUtil.formatWithDefaultPattern(startTime));
            } catch (ClassNotFoundException e) {
                if (logger.isDebugEnabled()) e.printStackTrace();
                logger.error("CloverJobBeanDefinitionParser-->>doParseElement() error," + e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (!StringUtil.isEmpty(startTime) && StringUtil.isEmpty(cronExpression)) {
            try {
                clientJob = ClientJobBuilder.quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndStartDate(jobGroup,
                        jobName,(Class<? extends Job>) Class.forName(id),
                        ClientJob.JobType.valueOf(jobType), ClientJob.ExecuteType.valueOf(executeType),
                        ClientJob.JobStrategy.valueOf(jobStrategy),
                        DateUtil.formatWithDefaultPattern(startTime));
            } catch (ClassNotFoundException e) {
                if (logger.isDebugEnabled()) e.printStackTrace();
                logger.error("CloverJobBeanDefinitionParser-->>doParseElement() error," + e.getMessage());
                throw new RuntimeException(e);
            }

        } else if (StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(cronExpression)) {
            try {
                clientJob = ClientJobBuilder.quickBuildJobWithJobTypeAndExecuteTypeAndJobStrategyAndCronExpression(jobGroup,
                        jobName,(Class<? extends Job>) Class.forName(id),
                        ClientJob.JobType.valueOf(jobType), ClientJob.ExecuteType.valueOf(executeType),
                        ClientJob.JobStrategy.valueOf(jobStrategy), cronExpression);
            } catch (ClassNotFoundException e) {
                if (logger.isDebugEnabled()) e.printStackTrace();
                logger.error("CloverJobBeanDefinitionParser-->>doParseElement() error," + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            logger.error("CloverJobBeanDefinitionParser-->>postProcessAfterInitialization startTime &  cronExpression  can not null at the same time");
            throw new RuntimeException("CloverJobBeanDefinitionParser-->>postProcessAfterInitialization startTime &  cronExpression  can not null at the same time");
        }
        String fixedClientIpsStr = element.getAttribute("fixedClientIps");
        if (!StringUtil.isEmpty(fixedClientIpsStr) && !fixedClientIpsStr.contains("，")) {
            clientJob.setFixedClientIps(fixedClientIpsStr.split(","));
        } else if (!StringUtil.isEmpty(fixedClientIpsStr) && fixedClientIpsStr.contains("，")) {
            logger.error("CloverJobBeanDefinitionParser-->>fixedClientIps can not include chinese comma");
            throw new RuntimeException("CloverJobBeanDefinitionParser-->>fixedClientIps can not include chinese comma");
        }
        String fixedServerIpsStr = element.getAttribute("fixedServerIps");
        if (!StringUtil.isEmpty(fixedServerIpsStr) && !fixedClientIpsStr.contains("，")) {
            clientJob.setFixedClientIps(fixedServerIpsStr.split(","));
        } else if (!StringUtil.isEmpty(fixedServerIpsStr) && fixedServerIpsStr.contains("，")) {
            logger.error("CloverJobBeanDefinitionParser-->>fixedServerIps can not include chinese comma");
            throw new RuntimeException("CloverJobBeanDefinitionParser-->>fixedServerIps can not include chinese comma");
        }
        //创建ClientJob 结束
        ModuleSchedulerClient.getInstance().handlerJob(clientJob);
    }
}
