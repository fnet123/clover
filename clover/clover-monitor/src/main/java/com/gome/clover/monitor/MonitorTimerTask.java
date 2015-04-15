package com.gome.clover.monitor;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.*;
import com.gome.clover.common.zk.ZKManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

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
 * Module Desc:Monitor Server Timer Task
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */
public class MonitorTimerTask extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(MonitorTimerTask.class);

    @Override
    public void run() {
        ZKManager zkManager = null;
        try {
            zkManager = new ZKManager(PropertiesUtil.loadProperties());
            processServerMonitor(zkManager);
            processClientMonitor(zkManager);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            System.err.println("MonitorServerTask-->>run() error" + e.getMessage());
            logger.error("MonitorServerTask-->>run() error", e);
        } finally {
            assert null != zkManager;
            try {
                zkManager.close();
            } catch (InterruptedException e) {
                if (logger.isDebugEnabled()) e.printStackTrace();
                System.err.println("MonitorServerTask-->>run()  zkManager.close() error" + e.getMessage());
                logger.error("MonitorServerTask-->>run()  zkManager.close() error", e);
            }
        }
    }

    private void processServerMonitor(ZKManager zkManager) {
        String serverPathStr = zkManager.getRootPath() + "/" + CommonConstants.MODULE_TYPE_SERVER + "/ip";
        String serverDataJsonStr;
        try {
            serverDataJsonStr = zkManager.getData(serverPathStr);
            if (StringUtil.isEmpty(serverDataJsonStr)) return;
            List<String> serverDataList = List2StringUtil.json2List(serverDataJsonStr);
            String remoteServerDataStr = null;
            for (String tempServerDataStr : serverDataList) {
                String[] tempSplitServerDatas = tempServerDataStr.split(CommonConstants.SPLIT_CHARACTER_FALG);
                Long clientLastUpdateTimeMillis = Long.valueOf(tempSplitServerDatas[2]);
                Long currentTimeMillis = System.currentTimeMillis(); //系统时间
                if (currentTimeMillis - clientLastUpdateTimeMillis > CommonConstants.SERVER_DIFFER_MILLI_SECONDS
                        || clientLastUpdateTimeMillis - currentTimeMillis > CommonConstants.SERVER_DIFFER_MILLI_SECONDS) {
                    String msg = " Server of Ip:" + tempSplitServerDatas[0] + " not alive,Please deal with as quickly as possible";
                    System.err.println(msg);
                    logger.error(msg);
                    //报警通知相关人员
                    SendMailUtil.sendDefaultMail(msg, msg);
                    remoteServerDataStr = tempServerDataStr+":";
                }
            }
            if(!StringUtil.isEmpty(remoteServerDataStr)){
                String removeServerDataStrs[] = remoteServerDataStr.split(":");
                for(String tempRemoveServerData:removeServerDataStrs){
                    if(!StringUtil.isEmpty(tempRemoveServerData)){
                        serverDataList.remove(tempRemoveServerData);
                    }

                }
            }
            if (null != serverDataList && serverDataList.size() > 0) {
                zkManager.setData(serverPathStr, JSON.toJSONString(serverDataList));// /clover/client/ip/127.0.0.1/{alive=1, ip=127.0.0.1}
            } else {
                zkManager.delete(serverPathStr);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void processClientMonitor(ZKManager zkManager) throws Exception {
        String pathStr = zkManager.getRootPath() + "/" + CommonConstants.MODULE_TYPE_CLIENT + "/ip";
        List<String> childrenIpStrList = zkManager.getZooKeeper().getChildren(pathStr, false);
        Map<String, String> clientMap;
        if (null != childrenIpStrList && childrenIpStrList.size() > 0) {
            for (String childrenIpStr : childrenIpStrList) {
                String dataJsonStr = zkManager.getData(pathStr + "/" + childrenIpStr);
                if (!StringUtil.isEmpty(dataJsonStr)) {
                    clientMap = JSON.parseObject(dataJsonStr, Map.class);
                    Long clientLastUpdateTimeMillis = Long.valueOf(clientMap.get(DBTableInfo.COL_TS));
                    Long currentTimeMillis = System.currentTimeMillis(); //系统时间
                    if (currentTimeMillis - clientLastUpdateTimeMillis > CommonConstants.CLIENT_DIFFER_MILLI_SECONDS
                            || clientLastUpdateTimeMillis - currentTimeMillis > CommonConstants.CLIENT_DIFFER_MILLI_SECONDS) {
                        System.err.println("Client Job of Ip:" + clientMap.get(DBTableInfo.COL_IP) + " not alive,Please deal with as quickly as possible");
                        String msg = "Client Job of Ip:" + clientMap.get(DBTableInfo.COL_IP) + " not alive,Please deal with as quickly as possible";
                        logger.error(msg);
                        //报警通知相关人员
                        alert(clientMap.get(DBTableInfo.COL_IP), msg);
                        clientMap.put(CommonConstants.ALIVE, CommonConstants.ALIVE_STATUS_0);
                        String clientPathStr = zkManager.getRootPath() + "/" + CommonConstants.MODULE_TYPE_CLIENT +
                                "/ip/" + clientMap.get(DBTableInfo.COL_IP);
                        zkManager.setData(clientPathStr, JSON.toJSONString(clientMap));// /clover/client/ip/127.0.0.1/{alive=1, ip=127.0.0.1}
                    }
                }
            }
        }
    }

    private void alert(String ip, String msg) {
        //发送邮件通知相关人员
        List<String> toList = new ArrayList<String>();
        DBCollection dbCollection = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_CONTACT);
        DBObject condition = new BasicDBObject();
        condition.put(DBTableInfo.COL_IP, ip);
        DBCursor cursorDocMap = dbCollection.find(condition);
        while (cursorDocMap.hasNext()) {
            DBObject tempDBObject = cursorDocMap.next();
            List2StringUtil.addUnDuplicateString2List((String) tempDBObject.get(DBTableInfo.COL_IP), toList);
        }
        List2StringUtil.addUnDuplicateString2List(CommonConstants.DEFAULT_COMPANY_EMAIL, toList);
        List2StringUtil.addUnDuplicateString2List(CommonConstants.DEFAULT_PRIVATE_EMAIL, toList);
        SendMailUtil.sendMails(List2StringUtil.listToArray(toList), msg, msg);
    }
}
