package com.gome.clover.common.zeromq;

import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
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
 * Module Desc:Publisher
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public enum  ZeroMQPub {
    INSTANCE;
    private static  final Logger logger = LoggerFactory.getLogger(ZeroMQPub.class);
    public boolean send(String addr,String msg,String topic){
        try{
            ZMQ.Context context = ZMQ.context(1);  //创创建包含一个I/O线程的context
            ZMQ.Socket publisher = context.socket(ZMQ.PUB);   //创建一个publisher类型的socket，他可以向所有订阅的subscriber广播数据
            publisher.bind(addr);  //将当前publisher绑定到5555端口上，可以接受subscriber的订阅
            //publisher.subscribe(topic.getBytes());
            String newMsg = topic.getBytes()+" "+msg;
            publisher.send(newMsg.getBytes(),ZMQ.NOBLOCK);
            publisher.close();
            context.term();
            return true;
        }catch (Exception e){
            logger.error("ZeroMQPub-->>send("+addr+","+msg+","+topic+")");
            return false;
        }
    }
    public static void main(String args[]) {
        String addr="tcp://*:"+ CommonConstants.ZMQ_SERVER_PORT;

        String topic=CommonConstants.TOPIC_CLOVER_CLIENT+"@"/*+IpUtil.getLocalIP()*/;
       while (true){
           ZeroMQPub.INSTANCE.send(addr, DateUtil.currentDateTime(),topic);
       }
    }
}