package com.gome.bg.clover.client.module;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.ConfigUtil;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.core.module.ModuleSchedulerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
 * Module Desc:Scheduler 客户端 初始化 Servlet
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */
@Component("SchedulerClientInitial")
public class SchedulerClientInitial extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SchedulerClientInitial.class);
    
    public static  String  token = ConfigUtil.getInstance("job.properties").getValue("token");
     
    public static  String  systemId = ConfigUtil.getInstance("job.properties").getValue("systemId");
    
    public static  String  jobClassStr = ConfigUtil.getInstance("job.properties").getValue("jobClassStr");
   
    public static  String  isRegisterToZK = ConfigUtil.getInstance("job.properties").getValue("isRegisterToZK");
    
    public static  String  isStartupNetty = ConfigUtil.getInstance("job.properties").getValue("isStartupNetty");


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        logger.error("token>>"+token+"*****systemId>>"+systemId+"*****jobClassStr>>"+jobClassStr+"*****isRegisterToZK>>"+isRegisterToZK+"*****isStartupNetty>>"+isStartupNetty);
       // String token =  config.getInitParameter("token");
        String token =  this.token;
        if (!StringUtil.isEmpty(token)) {
            //String systemId =  config.getInitParameter("systemId");
            String systemId = this.systemId;
            if(StringUtil.isEmpty(systemId))  throw new RuntimeException("SchedulerClientInitial-->>init(config) systemId null  ");
            List<String> jobClassList = null;
            //String  jobClassStr = config.getInitParameter("jobClassStr");
            String  jobClassStr = this.jobClassStr;
            boolean  isRegisterToZK = true; //默认是启动的
            //String isRegisterToZKStr =  config.getInitParameter("isRegisterToZK");
            String isRegisterToZKStr =  this.isRegisterToZK;
            if(!StringUtil.isEmpty(isRegisterToZKStr)){
                isRegisterToZK = Boolean.valueOf(isRegisterToZKStr);
            }
            //String isStartupNetty =  config.getInitParameter("isStartupNetty");
            String isStartupNetty = this.isStartupNetty;
            boolean  isStartupNe = true; //默认是启动的
            if(!StringUtil.isEmpty(isStartupNetty)){
                isStartupNe = Boolean.valueOf(isStartupNetty);
            }
            if(!StringUtil.isEmpty(jobClassStr)){
                String jobClassStrs[] = jobClassStr.split(",");
                jobClassList = new ArrayList<String>();
                for(String tempJobClass : jobClassStrs){
                    if(!StringUtil.isEmpty(tempJobClass)){
                        jobClassList.add(tempJobClass) ;
                    }
                }
            }
            //String port =  config.getInitParameter("port");
            ModuleSchedulerClient client =  ModuleSchedulerClient.getInstance();
            client.startup(jobClassList, isRegisterToZK, isStartupNe, String.valueOf(CommonConstants.NETTY_SERVER_PORT),systemId,token);
        }else {
            throw new RuntimeException("SchedulerClientInitial-->>init(config) token null  ");
        }
        logger.info("SchedulerClientInitial-->>init(config)......");
    }

    public void init(List<String> jobClassList, boolean isRegisterToZK, boolean isStartupMQ,String port,String systemId,String token) throws ServletException {
        try {
            ModuleSchedulerClient client =  ModuleSchedulerClient.getInstance();
            client.startup(jobClassList,isRegisterToZK,isStartupMQ,port,systemId,token);
        } catch (Exception e) {
            String execMethod = "SchedulerClientInitial-->>init("+ JSON.toJSONString(jobClassList)+")";
            String execResult = "SchedulerClientInitial-->>init("+JSON.toJSONString(jobClassList)+") error ,"+e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
        }
        logger.info("SchedulerClientInitial-->>init("+JSON.toJSONString(jobClassList)+")......");
    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SchedulerClientInitial() {
        super();
    }
}
