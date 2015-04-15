package com.gome.clover.zeromq;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
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
 * Module Desc:clover
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/4
 * Time: 20:28
 */
public class Pub {
    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        Context context = ZMQ.context(1);
        Socket publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:5557");
        publisher.send("B".getBytes(), ZMQ.SNDMORE);
        publisher.send("This is B".getBytes(), 0);

       /* int i = 0;
        while (true) {
            Thread.currentThread().sleep(1000);
            publisher.send("A".getBytes(), ZMQ.SNDMORE);
            publisher.send("This is A".getBytes(), 0);
            publisher.send("B".getBytes(), ZMQ.SNDMORE);
            publisher.send("This is B".getBytes(), 0);
        }*/
    }
}
