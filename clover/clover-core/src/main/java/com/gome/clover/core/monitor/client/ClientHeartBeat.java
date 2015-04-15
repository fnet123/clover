package com.gome.clover.core.monitor.client;

import com.gome.clover.common.systeminfo.SystemInfoUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.common.zk.ZKUtil;
import com.mongodb.BasicDBObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

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
 * Module Desc:Monitor Client Heart Beat
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */
public enum ClientHeartBeat {
    INSTNACE;
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private static boolean isStop = false;
    private static CuratorFramework curatorFramework = ZKUtil.create();
    public void startup() {
        if (null == scheduledThreadPoolExecutor) {
            scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        }
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!isStop) {
                    if (!curatorFramework.isStarted()) curatorFramework.start();
                    String clientPathStr = CommonConstants.ZK_ROOT_PATH + "/" + CommonConstants.MODULE_TYPE_CLIENT;
                    List clientNodeList = ZKUtil.getChilds(curatorFramework, clientPathStr);
                    if (null != clientNodeList && clientNodeList.size() > 0) {
                        updateRecordList(curatorFramework, clientPathStr, clientNodeList);
                    }

                }
            }
        }, CommonConstants.CLIENT_DIFFER_MILLI_SECONDS, CommonConstants.CLIENT_DIFFER_MILLI_SECONDS, TimeUnit.MILLISECONDS);
    }

    private void updateRecordList(CuratorFramework curatorFramework, String clientPathStr, List clientNodeList) {
        for (int i = 0; (clientNodeList != null) && (i < clientNodeList.size()); i++) {
            String id = (String) clientNodeList.get(i);
            String c = ZKUtil.getData(curatorFramework, clientPathStr + "/" + id);
            if (c == null) {
                continue;
            }
            BasicDBObject record = (BasicDBObject) com.mongodb.util.JSON.parse(c);
            String ip = (String) record.get(CommonConstants.IP);
            if (!StringUtil.isEmpty(ip) && IpUtil.getLocalIP().equals(ip)) {
                record.put(CommonConstants.MEM_RATIO, SystemInfoUtil.getMemRatio());
                record.put(CommonConstants.CPU_RATIO, SystemInfoUtil.getCpuRatio());
                record.put(CommonConstants.TOTAL_THREAD, SystemInfoUtil.getTotalThread());
                record.put(CommonConstants.TS, DateUtil.currentDateTime());
                ZKUtil.setPath(curatorFramework, clientPathStr + "/" + id, record.toString(), CreateMode.EPHEMERAL);
            }
        }
    }

    public void stop() {
        isStop = true;
        curatorFramework.close();
        scheduledThreadPoolExecutor.shutdown();
    }

    public static void main(String args[]) {
        ClientHeartBeat.INSTNACE.startup();
    }
}
