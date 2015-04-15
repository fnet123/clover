package com.gome.clover.common.zeromq;

import com.gome.clover.common.tools.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
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
 * Module Desc:ASync Send Msg
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/29
 * Time: 21:23
 */
public class AsyncSendMsg {
    private static final Logger logger = LoggerFactory.getLogger(AsyncSendMsg.class);
    private static ThreadPoolExecutor pool = null; //线程池
    private static final int  POOL_SIZE = Runtime.getRuntime().availableProcessors()*2 ;

    public static boolean send(String ip, String port, String msg) {
        Socket socket = null;
        try {
            socket = new Socket(ip, Integer.valueOf(port));//为了防止出现 zk节点信息变更延迟而出现client端已停止服务,但server端还发消息给client端
            if (socket.isConnected()) {
                String addr = "tcp://" + ip + ":" + port;
                ZeroMQPush.INSTANCE.send(addr, msg);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            System.err.println("SyncSendMsg-->>send(String ip,String port,String msg) error," + e.getMessage());
            logger.error("SyncSendMsg-->>send(String ip,String port,String msg) error," + e.getMessage());
            return false;
        } finally {
            if (null != socket && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    if (logger.isDebugEnabled()) e.printStackTrace();
                    System.err.println("SyncSendMsg-->>send(String ip,String port,String msg) socket.close() error," + e.getMessage());
                    logger.error("SyncSendMsg-->>send(String ip,String port,String msg) socket.close() error," + e.getMessage());
                }
            }
        }
    }

    public static void sendWithThreadPool(String ip, String port, String msg) {
        try {
            if (null == pool) {
                pool = new ThreadPoolExecutor(CommonConstants.POOL_SIZE, CommonConstants.POOL_SIZE, 60L,
                        TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            }
           // pool.execute(new Thread(new HandlerAsyncSendMsg(ip, port, msg)));
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            System.err.println("SyncSendMsg-->>sendWithThreadPool(String ip,String port,String msg) error," + e.getMessage());
            logger.error("SyncSendMsg-->>sendWithThreadPool(String ip,String port,String msg) error," + e.getMessage());
        }
    }
    public static void sendWithClientThreadPool(String ip, String port, String msg) {
        try {
            if (null == pool) {
                pool = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 60L,
                        TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            }
           // pool.execute(new Thread(new HandlerAsyncSendMsg(ip, port, msg)));
        } catch (Exception e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            System.err.println("SyncSendMsg----------->>sendWithClientThreadPool(String ip,String port,String msg) error," + e.getMessage());
            logger.error("SyncSendMsg-->>sendWithClientThreadPool(String ip,String port,String msg) error," + e.getMessage());
        }
    }

}
