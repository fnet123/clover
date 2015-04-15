package com.gome.clover.common.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Module Desc:Queue Server
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public enum  QueueServer {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(QueueServer.class);

    public boolean startup(){
        int port = 50470;
        try {
            QueueDaemon queueDaemon = new QueueDaemon(port);
            queueDaemon.start();
            return true;
        }catch (Exception e){
            if(logger.isDebugEnabled())e.printStackTrace();
            logger.error("QueueServer-->>startup()",e);
        } finally {
            logger.info("QueueServer start with port "+port+" start now ~");
        }
        return false;
    }
    public boolean startup(int port){
        try {
            QueueDaemon queueDaemon = new QueueDaemon(port);
            queueDaemon.start();
            return true;
        }catch (Exception e){
            if(logger.isDebugEnabled())e.printStackTrace();
            logger.error("QueueServer-->>startup("+port+")",e);
        } finally {
            logger.info("QueueServer start with port "+port+" start now ~");
        }
        return false;
    }

    public static void main(String[] args) {
        int port = 50470;
        QueueDaemon queueDaemon = new QueueDaemon(port);
        logger.info("QueueServer start with port "+port+" start now ~");
        System.err.println("QueueServer start with port \"+port+\" start now ~");
        queueDaemon.start();
    }
}