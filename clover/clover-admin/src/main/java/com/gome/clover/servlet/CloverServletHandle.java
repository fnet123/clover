package com.gome.clover.servlet;

import com.alibaba.fastjson.JSONObject;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.common.zeromq.AsyncSendMsg;
import com.gome.clover.common.zeromq.ZeroMQEntity;
import com.gome.clover.common.zeromq.ZeroMQPull;
import com.gome.clover.common.zk.ZKUtil;
import com.gome.clover.core.module.ModuleSchedulerServer;
import com.gome.clover.core.monitor.server.ServerHeartBeat;
import com.gome.clover.monitor.MonitorHeartBeat;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.bson.types.ObjectId;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
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
 * Module Desc:Clover Servlet Handle
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/24
 * Time: 11:30
 */
public class CloverServletHandle extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CloverServletHandle.class);
    private static final CuratorFramework curatorFramework = ZKUtil.create();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        if ("handleJobAdd".equals(action)) {
            out.println(handleJobAdd(request));
        } else if ("handleJobUpdate".equals(action)) {

        } else if ("handleJobDataDelete".equals(action)) {
            String id = request.getParameter("id");
            out.println(handleJobDataDelete(id));
        } else if ("handleJobDelete".equals(action)) {
            String id = request.getParameter("id");
            out.println(handleJobDelete(id));
        } else if ("handleJobDetail".equals(action)) {
            String id = request.getParameter("id");
            out.println(handleJobDetail(id));
        } else if ("handleLogDetail".equals(action)) {
            String id = request.getParameter("id");
            out.println(handleLogDetail(id));
        } else if ("handleContactAdd".equals(action)) {
            out.println(handleContactAdd(request));
        } else if ("handleContactUpdate".equals(action)) {
            out.println(handleContactUpdate(request));
        } else if ("handleContactDelete".equals(action)) {
            out.println(handleContactDelete(request));
        } else if ("handleContactDetail".equals(action)) {
            out.println(handleContactDetail(request));
        } else if ("handleStartupCloverServer".equals(action)) {
            out.println(handleStartupCloverServer());
        } else if ("handleStopCloverServer".equals(action)) {
            out.println(handleStopCloverServer());
        } else if ("handleStartupServerHeartBeat".equals(action)) {
            out.println(handleStartupServerHeartBeat());
        } else if ("handleStopServerHeartBeat".equals(action)) {
            out.println(handleStopServerHeartBeat());
        } else if ("handleStartupMonitorHeartBeat".equals(action)) {
            out.println(handleStartupMonitorHeartBeat());
        } else if ("handleStopMonitorHeartBeat".equals(action)) {
            out.println(handleStopMonitorHeartBeat());
        }
        out.flush();
        out.close();
    }

    private String handleJobAdd(HttpServletRequest request) {
        try {
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("jobGroup", request.getParameter("jobGroup"));
            basicDBObject.put("jobName", request.getParameter("jobName"));
            basicDBObject.put("jobType", request.getParameter("jobType"));
            basicDBObject.put("jobStrategy", request.getParameter("jobStrategy"));
            basicDBObject.put("executeType", request.getParameter("executeType"));
            basicDBObject.put("jobClassName", request.getParameter("jobClassName"));
            basicDBObject.put("startTime", request.getParameter("startTime"));
            basicDBObject.put("cronExpression", request.getParameter("cronExpression"));
            String ip = request.getParameter("ip");
            String port = request.getParameter("port");
            basicDBObject.put("ip", ip);
            basicDBObject.put("port", port);
            basicDBObject.put("fixedClientIps", request.getParameter("fixedClientIps"));
            basicDBObject.put("fixedServerIps", request.getParameter("fixedServerIps"));
            String msg = JSON.serialize(basicDBObject);
            ZeroMQEntity zeroMQEntity = new ZeroMQEntity(CommonConstants.MODULE_TYPE_CLIENT_WITH_ADMIN, ip, null, msg);
            //AsyncSendMsg.send(ip, port, com.alibaba.fastjson.JSON.toJSONString(zeroMQEntity));
            return "1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) System.err.println(e.getMessage());
            logger.error("CloverServletHandle-->>handleJobAdd(HttpServletRequest request) error,", e);
            return "-1";
        }
    }

    private String handleJobDataDelete(String id) {
        try {
            return MongoDBUtil.INSTANCE.delete(new BasicDBObject(
                    DBTableInfo.COL_ID, new ObjectId(id)), DBTableInfo.TBL_CLOVER_JOB) ? "1" : "-1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) System.err.println(e.getMessage());
            logger.error("CloverServletHandle-->>handleContactDetail(" + id + ") error,", e);
            return "-1";
        }

    }

    private String handleJobDelete(String id) {
        try {
            DBObject dbObject = MongoDBUtil.INSTANCE.findOneByCondition(new BasicDBObject(
                    DBTableInfo.COL_ID, new ObjectId(id)), DBTableInfo.TBL_CLOVER_JOB);
            if (null != dbObject) {
                MongoDBUtil.INSTANCE.delete(new BasicDBObject(
                        DBTableInfo.COL_ID, new ObjectId(id)), DBTableInfo.TBL_CLOVER_JOB);
                String serverIp = (String) dbObject.get(DBTableInfo.COL_SERVER_IP);
                String jobKey = (String) dbObject.get(DBTableInfo.COL_JOB_KEY);
                String jobGroup = jobKey.substring(0, jobKey.lastIndexOf("."));
                String jobName = jobKey.substring(jobKey.lastIndexOf(".") + 1);
                if (IpUtil.getLocalIP().equals(serverIp)) {
                    try {
                        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                        JobKey jobKey4Delete = new JobKey(jobName, jobGroup);
                        if (scheduler.checkExists(jobKey4Delete)) {
                            return scheduler.deleteJob(jobKey4Delete) ? "1" : "-1";
                        }
                    } catch (SchedulerException e) {
                        if (logger.isDebugEnabled()) e.printStackTrace();
                        logger.error("CloverServletHandle-->>handleJobDelete(" + id + ") error," + e.getMessage());
                        return "-1";
                    }
                } else if (!StringUtil.isEmpty(serverIp)) {
                    BasicDBObject basicDBObject = new BasicDBObject();
                    basicDBObject.put("jobGroup", jobGroup);
                    basicDBObject.put("jobName", jobName);
                    String msg = JSON.serialize(basicDBObject);
                    ZeroMQEntity zeroMQEntity = new ZeroMQEntity(CommonConstants.MODULE_TYPE_SERVER_WITH_ADMIN, serverIp, null, msg);
                    String serverPathStr = CommonConstants.ZK_ROOT_PATH + "/" + CommonConstants.MODULE_TYPE_SERVER;
                    String port = ZKUtil.getDataByParameter(serverPathStr, "ip", serverIp, "port");
                    if (!StringUtil.isEmpty(port)) {
                        return "-1"; //AsyncSendMsg.send(serverIp, port, com.alibaba.fastjson.JSON.toJSONString(zeroMQEntity)) ? "1" : "-1";
                    } else {
                        return "-1";
                    }
                }
            } else {
                return "-1";
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) System.err.println(e.getMessage());
            logger.error("CloverServletHandle-->>handleJobDelete(" + id + ") error,", e);
            return "-1";
        }
        return "-1";
    }

    private String handleJobDetail(String id) {
        try {
            DBObject dbObject = MongoDBUtil.INSTANCE.findOneByCondition(new BasicDBObject(
                    DBTableInfo.COL_ID, new ObjectId(id)), DBTableInfo.TBL_CLOVER_JOB);
            if (null != dbObject) {
                //构造json对象
                JSONObject json = new JSONObject();
                json.put("jobKey", dbObject.get(DBTableInfo.COL_JOB_KEY));
                json.put("jobType", dbObject.get(DBTableInfo.COL_JOB_TYPE));
                json.put("executeType", dbObject.get(DBTableInfo.COL_EXECUTE_TYPE));
                json.put("jobStrategy", dbObject.get(DBTableInfo.COL_JOB_STRATEGY));
                json.put("startTime", dbObject.get(DBTableInfo.COL_START_TIME));
                String cronExpressionStr = (String) dbObject.get(DBTableInfo.COL_CRON_EXPRESSION);
                json.put("cronExpression", cronExpressionStr);
                String nextValidTime = "";
                if (!StringUtil.isEmpty(cronExpressionStr)) {
                    if (null != dbObject.get(DBTableInfo.COL_CRON_EXPRESSION)) {
                        try {
                            nextValidTime = DateUtil.formatWithDefaultPattern(
                                    DateUtil.getNextValidTimeAfter(cronExpressionStr, new Date()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            logger.error("CloverServletHandle-->>handleJobDetail(" + id + ") error," + e.getMessage());
                        }
                    }
                }
                json.put("nextValidTime", nextValidTime);
          /*  String classPath = System.getProperty("user.dir");
            MyClassLoader cl = new MyClassLoader(classPath);
            String className = "com.gome.clover.job.MyJobWithSimpleJob";
            CreateAndCompileClassFile.compileAndLoading(classPath,className);
            try {
                Class cls=cl.loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            json.put("jobInfo", JSON.toJSONString((ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64(
                    (String) dbObject.get(DBTableInfo.COL_JOB_INFO)))));*/
                json.put("ip", dbObject.get(DBTableInfo.COL_IP));
                json.put("fixedClientIps", dbObject.get(DBTableInfo.COL_FIXED_CLIENT_IPS));
                json.put("fixedServerIps", dbObject.get(DBTableInfo.COL_FIXED_SERVER_IPS));
                json.put("executeStartTime", dbObject.get(DBTableInfo.COL_EXECUTE_START_TIME));
                json.put("executeEndTime", dbObject.get(DBTableInfo.COL_EXECUTE_END_TIME));
                json.put("times", dbObject.get(DBTableInfo.COL_TIMES));
                json.put("failTimes", dbObject.get(DBTableInfo.COL_FAIL_TIMES));
                json.put("status", CommonConstants.JOB_STATUS_1.equals(dbObject.get(DBTableInfo.COL_STATUS)) ? "初始化状态" :
                        "运行中状态");
                json.put("ts", dbObject.get(DBTableInfo.COL_TS));
                return json.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) System.err.println(e.getMessage());
            logger.error("CloverServletHandle-->>handleJobDetail(" + id + ") error,", e);
            return "-1";
        }
    }

    private String handleLogDetail(String id) {
        try {
            DBObject dbObject = MongoDBUtil.INSTANCE.findOneByCondition(new BasicDBObject(
                    DBTableInfo.COL_ID, new ObjectId(id)), DBTableInfo.TBL_CLOVER_LOG);
            if (null != dbObject) {
                //构造json对象
                JSONObject json = new JSONObject();
                json.put("systemId", dbObject.get(DBTableInfo.COL_SYSTEM_ID));
                json.put("ip", dbObject.get(DBTableInfo.COL_IP));
                json.put("jobKey", dbObject.get(DBTableInfo.COL_JOB_KEY));
                json.put("execMethod", dbObject.get(DBTableInfo.COL_EXEC_METHOD));
                json.put("execResult", dbObject.get(DBTableInfo.COL_EXEC_RESULT));
                json.put("execTime", dbObject.get(DBTableInfo.COL_EXEC_TIME));
                json.put("ts", dbObject.get(DBTableInfo.COL_TS));
                return json.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) System.err.println(e.getMessage());
            logger.error("CloverServletHandle-->>handleLogDetail(" + id + ") error,", e);
            return "-1";
        }
    }

    private String handleContactAdd(HttpServletRequest request) {
        try {
            BasicDBObject document = new BasicDBObject();
            String systemId = request.getParameter("systemId");
            document.put(DBTableInfo.COL_SYSTEM_ID, StringUtil.isEmpty(systemId) ? "" : systemId);
            String ip = request.getParameter("ip");
            document.put(DBTableInfo.COL_IP, StringUtil.isEmpty(ip) ? "" : ip);
            String jobKey = request.getParameter("jobKey");
            document.put(DBTableInfo.COL_JOB_KEY, StringUtil.isEmpty(jobKey) ? "" : jobKey);
            String contacter = request.getParameter("contacter");
            document.put(DBTableInfo.COL_CONTACTER, StringUtil.isEmpty(contacter) ? "" : contacter);
            String email = request.getParameter("email");
            document.put(DBTableInfo.COL_EMAIL, StringUtil.isEmpty(email) ? "" : email);
            String mobile = request.getParameter("mobile");
            document.put(DBTableInfo.COL_MOBILE, StringUtil.isEmpty(mobile) ? "" : mobile);
            document.put(DBTableInfo.COL_TS, DateUtil.currentDateTime());
            return MongoDBUtil.INSTANCE.insertOrUpdate(document, DBTableInfo.TBL_CLOVER_CONTACT) ? "1" : "-1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) System.err.println(e.getMessage());
            logger.error("CloverServletHandle-->>handleContactAdd(HttpServletRequest request) error,", e);
            return "-1";
        }
    }

    private String handleContactUpdate(HttpServletRequest request) {
        try {
            BasicDBObject document = new BasicDBObject();
            String id = request.getParameter("id");
            String systemId = request.getParameter("systemId");
            document.put(DBTableInfo.COL_SYSTEM_ID, StringUtil.isEmpty(systemId) ? "" : systemId);
            String ip = request.getParameter("ip");
            document.put(DBTableInfo.COL_IP, StringUtil.isEmpty(ip) ? "" : ip);
            String jobKey = request.getParameter("jobKey");
            document.put(DBTableInfo.COL_JOB_KEY, StringUtil.isEmpty(jobKey) ? "" : jobKey);
            String contacter = request.getParameter("contacter");
            document.put(DBTableInfo.COL_CONTACTER, StringUtil.isEmpty(contacter) ? "" : contacter);
            String email = request.getParameter("email");
            document.put(DBTableInfo.COL_EMAIL, StringUtil.isEmpty(email) ? "" : email);
            String mobile = request.getParameter("mobile");
            document.put(DBTableInfo.COL_MOBILE, StringUtil.isEmpty(mobile) ? "" : mobile);
            document.put(DBTableInfo.COL_TS, DateUtil.currentDateTime());
            return MongoDBUtil.INSTANCE.update(new BasicDBObject(
                    DBTableInfo.COL_ID, new ObjectId(id)), document, DBTableInfo.TBL_CLOVER_CONTACT) ? "1" : "-1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) System.err.println(e.getMessage());
            logger.error("CloverServletHandle-->>handleContactUpdate(HttpServletRequest request) error,", e);
            return "-1";
        }
    }

    private String handleContactDelete(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            return MongoDBUtil.INSTANCE.delete(new BasicDBObject(
                    DBTableInfo.COL_ID, new ObjectId(id)), DBTableInfo.TBL_CLOVER_CONTACT) ? "1" : "-1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) System.err.println(e.getMessage());
            logger.error("CloverServletHandle-->>handleContactDelete(HttpServletRequest request) error,", e);
            return "-1";
        }
    }

    private String handleContactDetail(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            DBObject dbObject = MongoDBUtil.INSTANCE.findOneByCondition(new BasicDBObject(
                    DBTableInfo.COL_ID, new ObjectId(id)), DBTableInfo.TBL_CLOVER_CONTACT);
            if (null != dbObject) {
                //构造json对象
                JSONObject json = new JSONObject();
                json.put(DBTableInfo.COL_SYSTEM_ID, dbObject.get(DBTableInfo.COL_SYSTEM_ID));
                json.put(DBTableInfo.COL_IP, dbObject.get(DBTableInfo.COL_IP));
                json.put(DBTableInfo.COL_JOB_KEY, dbObject.get(DBTableInfo.COL_JOB_KEY));
                json.put(DBTableInfo.COL_CONTACTER, dbObject.get(DBTableInfo.COL_CONTACTER));
                json.put(DBTableInfo.COL_EMAIL, dbObject.get(DBTableInfo.COL_EMAIL));
                json.put(DBTableInfo.COL_MOBILE, dbObject.get(DBTableInfo.COL_MOBILE));
                json.put(DBTableInfo.COL_TS, dbObject.get(DBTableInfo.COL_TS));
                return json.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) System.err.println(e.getMessage());
            logger.error("CloverServletHandle-->>handleContactDetail(HttpServletRequest request) error,", e);
            return "-1";
        }
    }

    private String handleStartupCloverServer() {
        try {
            if (!curatorFramework.isStarted()) curatorFramework.start();
            String serverDataStr = ZKUtil.getData(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" +
                    IpUtil.getLocalIP());
            BasicDBObject serverData;
            if (!StringUtil.isEmpty(serverDataStr)) {
                serverData = (BasicDBObject) JSON.parse(serverDataStr);
                serverData.put("cloverServerStatus", "1");
                serverData.put("cloverServerTS", DateUtil.currentDateTime());
            } else {
                serverData = new BasicDBObject();
                serverData.put("cloverServerStatus", "1");
                serverData.put("cloverServerTS", DateUtil.currentDateTime());
            }
            ZKUtil.setPath(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" + IpUtil.getLocalIP(),
                    serverData.toString(), CreateMode.EPHEMERAL);
            ModuleSchedulerServer.getInstance().startup();
            return "1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("CloverServletHandle-->>handleStartupCloverServer() error,", e);
            return "-1";
        }
    }

    private String handleStopCloverServer() {
        try {
            if (!curatorFramework.isStarted()) curatorFramework.start();
            String serverDataStr = ZKUtil.getData(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" +
                    IpUtil.getLocalIP());
            BasicDBObject serverData;
            if (!StringUtil.isEmpty(serverDataStr)) {
                serverData = (BasicDBObject) JSON.parse(serverDataStr);
                serverData.put("cloverServerStatus", "0");
                serverData.put("cloverServerTS", DateUtil.currentDateTime());
            } else {
                serverData = new BasicDBObject();
                serverData.put("cloverServerStatus", "0");
                serverData.put("cloverServerTS", DateUtil.currentDateTime());
            }
            ZKUtil.setPath(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" + IpUtil.getLocalIP(),
                    serverData.toString(), CreateMode.EPHEMERAL);
            ModuleSchedulerServer.getInstance().stop();
            ZeroMQPull.INSTANCE.stop(IpUtil.getLocalIP(),CommonConstants.ZMQ_SERVER_PORT);
            return "1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("CloverServletHandle-->>handleStopCloverServer() error,", e);
            return "-1";
        }
    }

    private String handleStartupServerHeartBeat() {
        try {
            if (!curatorFramework.isStarted()) curatorFramework.start();
            String serverDataStr = ZKUtil.getData(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" +
                    IpUtil.getLocalIP());
            BasicDBObject serverData;
            if (!StringUtil.isEmpty(serverDataStr)) {
                serverData = (BasicDBObject) JSON.parse(serverDataStr);
                serverData.put("serverHeartBeatStatus", "1");
                serverData.put("serverHeartBeatTS", DateUtil.currentDateTime());
            } else {
                serverData = new BasicDBObject();
                serverData.put("serverHeartBeatStatus", "1");
                serverData.put("serverHeartBeatTS", DateUtil.currentDateTime());
            }
            ZKUtil.setPath(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" + IpUtil.getLocalIP(),
                    serverData.toString(), CreateMode.EPHEMERAL);
            ServerHeartBeat.INSTNACE.startup();
            return "1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("CloverServletHandle-->>startupMonitorHeartBeat() error,", e);
            return "-1";
        }
    }

    private String handleStopServerHeartBeat() {
        try {
            if (!curatorFramework.isStarted()) curatorFramework.start();
            String serverDataStr = ZKUtil.getData(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" +
                    IpUtil.getLocalIP());
            BasicDBObject serverData;
            if (!StringUtil.isEmpty(serverDataStr)) {
                serverData = (BasicDBObject) JSON.parse(serverDataStr);
                serverData.put("serverHeartBeatStatus", "0");
                serverData.put("serverHeartBeatTS", DateUtil.currentDateTime());
            } else {
                serverData = new BasicDBObject();
                serverData.put("serverHeartBeatStatus", "0");
                serverData.put("serverHeartBeatTS", DateUtil.currentDateTime());
            }
            ZKUtil.setPath(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" + IpUtil.getLocalIP(),
                    serverData.toString(), CreateMode.EPHEMERAL);
            ServerHeartBeat.INSTNACE.stop();
            return "1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("CloverServletHandle-->>startupMonitorHeartBeat() error,", e);
            return "-1";
        }
    }

    private String handleStartupMonitorHeartBeat() {
        try {
            if (!curatorFramework.isStarted()) curatorFramework.start();
            String serverDataStr = ZKUtil.getData(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" +
                    IpUtil.getLocalIP());
            BasicDBObject serverData;
            if (!StringUtil.isEmpty(serverDataStr)) {
                serverData = (BasicDBObject) JSON.parse(serverDataStr);
                serverData.put("monitorHeartBeatStatus", "1");
                serverData.put("monitorHeartBeatTS", DateUtil.currentDateTime());
            } else {
                serverData = new BasicDBObject();
                serverData.put("monitorHeartBeatStatus", "1");
                serverData.put("monitorHeartBeatTS", DateUtil.currentDateTime());
            }
            ZKUtil.setPath(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" + IpUtil.getLocalIP(),
                    serverData.toString(), CreateMode.EPHEMERAL);
            MonitorHeartBeat.INSTNACE.startup();
            return "1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("CloverServletHandle-->>startupMonitorHeartBeat() error,", e);
            return "-1";
        }
    }

    private String handleStopMonitorHeartBeat() {
        try {
            if (!curatorFramework.isStarted()) curatorFramework.start();
            String serverDataStr = ZKUtil.getData(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" +
                    IpUtil.getLocalIP());
            BasicDBObject serverData;
            if (!StringUtil.isEmpty(serverDataStr)) {
                serverData = (BasicDBObject) JSON.parse(serverDataStr);
                serverData.put("monitorHeartBeatStatus", "0");
                serverData.put("monitorHeartBeatTS", DateUtil.currentDateTime());
            } else {
                serverData = new BasicDBObject();
                serverData.put("monitorHeartBeatStatus", "0");
                serverData.put("monitorHeartBeatTS", DateUtil.currentDateTime());
            }
            ZKUtil.setPath(curatorFramework, CommonConstants.ZK_ROOT_PATH + "/monitor/server/" + IpUtil.getLocalIP(),
                    serverData.toString(), CreateMode.EPHEMERAL);
            MonitorHeartBeat.INSTNACE.stop();
            return "1";
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("CloverServletHandle-->>startupMonitorHeartBeat() error,", e);
            return "-1";
        }
    }
}
