package com.gome.clover.zeromq;

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

public class Publisher {
    public static void main(String args[]) {

        ZMQ.Context context = ZMQ.context(1);  //创创建包含一个I/O线程的context
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);   //创建一个publisher类型的socket，他可以向所有订阅的subscriber广播数据

        publisher.bind("tcp://127.0.0.1:5555");  //将当前publisher绑定到5555端口上，可以接受subscriber的订阅

        while (!Thread.currentThread ().isInterrupted ()) {
            String message = "fjs hello";  //最开始可以理解为pub的channel，subscribe需要订阅fjs这个channel才能接收到消息
            publisher.send(message.getBytes());
        }

        publisher.close();
        context.term();
    }
}