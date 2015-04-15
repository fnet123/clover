package com.gome.clover.common.annotation;

import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.core.module.ModuleSchedulerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

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
 * Module Desc:Clover Config Bean Definition Parser
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/3
 * Time: 17:53
 */
public class CloverConfigBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final Logger logger = LoggerFactory.getLogger(CloverConfigBeanDefinitionParser.class);
    private static final String CLOVERJOB_ANNOTATION = "CLOVERJOB_ANNOTATION";
    private static final String CLOVER_CONFIG_INIT = "CLOVER_CONFIG_INIT";
    private volatile boolean init = false;
    @Override
    protected Class<?> getBeanClass(Element element) {
        return CloverConfigInit.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);
        if (init) {
            throw new RuntimeException("一个应用中只允许配置一个<cloverjob:config>");
        }
        builder.setInitMethodName("init");
        builder.setDestroyMethodName("destroy");
        builder.addPropertyValue("element", element);
        doParseJobConfig(element);
        if (!parserContext.getRegistry().containsBeanDefinition(CLOVERJOB_ANNOTATION)) {
            RootBeanDefinition annotation = new RootBeanDefinition(CloverJobAnnotationBean.class);
            parserContext.getRegistry().registerBeanDefinition(CLOVERJOB_ANNOTATION, annotation);
        }
        init = true;
    }

    private void doParseJobConfig(Element element) {
        //解析 spring clover config:config标签配置元素 开始
        String token = element.getAttribute("token");
        if (!StringUtil.isEmpty(token) && CommonConstants.token.equals(token)) {
            String systemId = element.getAttribute("systemId");
            if(StringUtil.isEmpty(systemId))  throw new RuntimeException("CloverConfigBeanDefinitionParser-->>doParseJobConfig(element) systemId null  ");
            String port = element.getAttribute("port");
            boolean isStartupNetty = Boolean.valueOf(element.getAttribute("isStartupNetty"));
            boolean isRegisterToZK = Boolean.valueOf(element.getAttribute("isRegisterToZK"));
            String jobClassName = element.getAttribute("jobClassName");
            if (StringUtil.isEmpty(jobClassName)) {
                logger.error("CloverConfigBeanDefinitionParser-->>doParseJobConfig(element) jobClassName can not null ");
                throw new RuntimeException("CloverConfigBeanDefinitionParser-->>doParseJobConfig(element) jobClassName can not null ");
            } else {
                List<String> jobClassList = new ArrayList<String>();
                if (!StringUtil.isEmpty(jobClassName) && !jobClassName.contains("，")) {
                    String jobClassNames[] = jobClassName.split(",");
                    for (String jobClassNameStr : jobClassNames) {
                        if (!StringUtil.isEmpty(jobClassNameStr)) {
                            jobClassList.add(jobClassNameStr.trim());
                        }
                    }
                } else if (!StringUtil.isEmpty(jobClassName) && jobClassName.contains("，")) {
                    logger.error("CloverConfigBeanDefinitionParser-->>doParseJobConfig(element) jobClassName  can not include chinese comma");
                    throw new RuntimeException("CloverConfigBeanDefinitionParser-->>doParseJobConfig(element) jobClassName  can not include chinese comma");
                }
                ModuleSchedulerClient client = ModuleSchedulerClient.getInstance();
                client.startup(jobClassList, isRegisterToZK, isStartupNetty, port,systemId,token);
            }
        } else if (!StringUtil.isEmpty(token) && !CommonConstants.token.equals(token)) {
            throw new RuntimeException("CloverConfigBeanDefinitionParser-->>doParseJobConfig() token wrong  ");
        } else if (StringUtil.isEmpty(token)) {
            throw new RuntimeException("CloverConfigBeanDefinitionParser-->>doParseJobConfig() token null  ");
        }
        //解析 spring cloverjob:config标签配置元素 结束
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
        return  CLOVER_CONFIG_INIT;
    }
}
