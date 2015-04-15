package com.gome.clover.common.zeromq;
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
 * Module Desc:Subscriber
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

public enum  ZeroMQSub {
    INSTANCE;
    private static  final Logger logger = LoggerFactory.getLogger(ZeroMQPub.class);
    public void recv(String addr){
        try {
            ZMQ.Context context = ZMQ.context(1);  //创建1个I/O线程的上下文
            ZMQ.Socket subscriber = context.socket(ZMQ.SUB);     //创建一个sub类型，也就是subscriber类型的socket
            subscriber.connect(addr);    //监听的publisher建立连接
            subscriber.subscribe((CommonConstants.TOPIC_CLOVER_CLIENT+"@"/*+IpUtil.getLocalIP()*/).getBytes());     //订阅这个channel
            while (true){
                String msg = new String(subscriber.recv());//接收publisher发送过来的消息
                System.err.println("receive : " +msg);
            }
        }catch (Exception e){
            logger.error("ZeroMQSub-->>recv("+addr+")");
        }

    }
    public static void main(String args[]) {
        String addr="tcp://"+ IpUtil.getLocalIP()+":"+ CommonConstants.ZMQ_SERVER_PORT;
        ZeroMQSub.INSTANCE.recv(addr);
        System.err.println("ZeroMQSub-----");
    }
}
