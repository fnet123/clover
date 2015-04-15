package com.gome.clover.zeromq;

import com.gome.clover.common.tools.RandomNumUtil;
import org.junit.Test;
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
 * Module Desc:clover
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/4
 * Time: 21:07
 */
public class RandomNum {
    @Test
    public void testGetRandomNum(){
        while (true){
            System.err.println("ThreadLocalRandom.current().nextInt(0,65535) time:"+ RandomNumUtil.getNextLong());
        }

    }

    @Test
    public void testConnection(){

        ZMQ.Context context = ZMQ.context(1);


        ZMQ.Socket socket = context.socket(ZMQ.REP);
        socket.connect("tcp://127.0.0.1:6752");
    }
}
