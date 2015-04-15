package com.gome.clover.common.zeromq;

import com.gome.clover.common.mongodb.BuildMongoDBData;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Module Desc:ASync Start Zero MQ
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/29
 * Time: 21:23
 */
public class AsyncStartZeroMQ implements Runnable {
    private static  final Logger logger = LoggerFactory.getLogger(ZeroMQPub.class);
    private String port;
    public AsyncStartZeroMQ(String port){
        this.port = port;
    }
    public static void startup(String port){
        new Thread(new AsyncStartZeroMQ(port)).start();
    }
    @Override
    public void run() {
        try {
            Thread.sleep(CommonConstants.ZMQ_SLEEP_CLIENT_MILLIS);
            String addr = "tcp://"+ IpUtil.getLocalIP()+":" + port;
            ZeroMQPull.INSTANCE.recv(addr);
        } catch (InterruptedException e) {
            logger.error("SyncStartZeroMQ-->>run() error",e);
            String execMethod = "ModuleSchedulerServer-->>run()";
            String execResult = "ModuleSchedulerServer-->>run() error ," + e.getMessage();
            MongoDBUtil.INSTANCE.insert(BuildMongoDBData.getInsertLogBasicDBObject("",
                    execMethod, execResult), DBTableInfo.TBL_CLOVER_LOG);
        }
    }

    public static void  main(String args[]){
        AsyncStartZeroMQ.startup(CommonConstants.ZMQ_SERVER_PORT);
        System.err.println("SyncStartZeroMQ");
        try {
            TimeUnit.SECONDS.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ZeroMQPull.INSTANCE.stop();
        System.err.println("stop finish....");
    }
}
