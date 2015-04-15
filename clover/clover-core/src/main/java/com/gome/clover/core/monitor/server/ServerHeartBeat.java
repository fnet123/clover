package com.gome.clover.core.monitor.server;

import com.gome.clover.common.systeminfo.SystemInfoUtil;
import com.gome.clover.common.tools.*;
import com.gome.clover.common.zk.ZKManager;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
 * Module Desc: Server Heart Beat
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */
public enum ServerHeartBeat {
    INSTNACE;
    private static final Logger logger = LoggerFactory.getLogger(ServerHeartBeat.class);
    private static   ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private static   ZKManager zkManager = null;
    private static boolean isStop = false;
    static {
        try {
            zkManager = new ZKManager(PropertiesUtil.loadProperties());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ServerHeartBeat-->> static method zkManager = new ZKManager(PropertiesUtil.loadProperties()) error ");
        }
    }
   public void startup(){
       if(null==scheduledThreadPoolExecutor){
           scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
       }
       scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
           @Override
           public void run() {
               if (!isStop) {
                   try {
                       String serverPathStr = CommonConstants.ZK_ROOT_PATH + "/" + CommonConstants.MODULE_TYPE_SERVER;
                       List serverNodeList = zkManager.getZooKeeper().getChildren(serverPathStr, false);
                       if (null != serverNodeList && serverNodeList.size() > 0) {
                           updateRecordList(zkManager, serverPathStr, serverNodeList);
                       }
                   } catch (Exception e) {
                       if (logger.isDebugEnabled()) e.printStackTrace();
                       System.err.println("ServerHeartBeat-->>run()  error，"+e.getMessage());
                       logger.error("ServerHeartBeat-->>run()  error", e);
                   }
               }
           }
       }, CommonConstants.SERVER_DIFFER_MILLI_SECONDS, CommonConstants.SERVER_DIFFER_MILLI_SECONDS, TimeUnit.MILLISECONDS);
   }
    private void updateRecordList(ZKManager zkManager, String serverPathStr, List serverNodeList) throws Exception {
        for (int i = 0; (serverNodeList != null) && (i < serverNodeList.size()); i++) {
            String id = (String) serverNodeList.get(i);
            String c = zkManager.getData(serverPathStr + "/" + id);
            if (c == null) {
                continue;
            }
            BasicDBObject record = (BasicDBObject) com.mongodb.util.JSON.parse(c);
            String ip = (String) record.get(CommonConstants.IP);
            if(!StringUtil.isEmpty(ip) && IpUtil.getLocalIP().equals(ip)){
                record.put(CommonConstants.MEM_RATIO, SystemInfoUtil.getMemRatio());
                record.put(CommonConstants.CPU_RATIO, SystemInfoUtil.getCpuRatio());
                record.put(CommonConstants.TOTAL_THREAD, SystemInfoUtil.getTotalThread());
                record.put(CommonConstants.TS, DateUtil.currentDateTime());
                zkManager.setData(serverPathStr + "/" + id,record.toString());
            }
        }
    }
    public void stop() {
        isStop = true;
        assert null!=zkManager;
        try {
            zkManager.close();
        } catch (InterruptedException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            System.err.println("ServerHeartBeat-->>stop()  zkManager.close() error"+ e.getMessage());
            logger.error("ServerHeartBeat-->>stop()  zkManager.close() error",e);
        }
        scheduledThreadPoolExecutor.shutdown();
    }
    public static void main(String args[]){
        ServerHeartBeat.INSTNACE.startup();
        try {
            TimeUnit.MINUTES.sleep(4L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServerHeartBeat.INSTNACE.stop();
    }
}
