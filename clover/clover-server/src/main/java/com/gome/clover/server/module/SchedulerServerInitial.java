package com.gome.clover.server.module;

import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.core.module.ModuleSchedulerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Module Desc: Scheduler 服务端 初始化 Servlet
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class SchedulerServerInitial extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SchedulerServerInitial.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        //super.init(config);
        String systemId =  config.getInitParameter("systemId");
        if(StringUtil.isEmpty(systemId))  systemId = CommonConstants.SYSTEM_ID_CLOVER;
        boolean  isRegisterToZK = true; //默认是启动的
        String isRegisterToZKStr =  config.getInitParameter("isRegisterToZK");
        if(!StringUtil.isEmpty(isRegisterToZKStr)){
            isRegisterToZK = Boolean.valueOf(isRegisterToZKStr);
        }
        boolean  isStartupMQ = true; //默认是启动的
        boolean  isStartupNetty = true; //默认是启动的
        String isStartupMQStr =  config.getInitParameter("isStartupMQ");
        if(!StringUtil.isEmpty(isStartupMQStr)){
            isStartupMQ = Boolean.valueOf(isStartupMQStr);
        }
        boolean  isReloadJobFromDB = true; //默认是启动的
        String isReloadJobFromDBStr =  config.getInitParameter("isReloadJobFromDB");
        if(!StringUtil.isEmpty(isReloadJobFromDBStr)){
            isReloadJobFromDB = Boolean.valueOf(isReloadJobFromDBStr);
        }
        try {
            ModuleSchedulerServer server =  ModuleSchedulerServer.getInstance();
            server.startup(isRegisterToZK,isStartupMQ,isStartupNetty,isReloadJobFromDB,systemId);
        } catch (Exception e) {
            String execMethod = "ModuleSchedulerServer-->>init(config)";
            String execResult = "ModuleSchedulerServer-->>init(config) error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            System.err.println("SchedulerServerInitial-->>init(config) error ,"+e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("SchedulerServerInitial-->>init(config)......");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ModuleSchedulerServer server =  ModuleSchedulerServer.getInstance();
            server.startup();
        } catch (Exception e) {
            String execMethod = "ModuleSchedulerServer-->>init()";
            String execResult = "ModuleSchedulerServer-->>init() error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
            System.err.println("SchedulerServerInitial-->>init() error ,"+e.getMessage());
        }
        logger.info("SchedulerServerInitial-->>init()......");
    }
}
