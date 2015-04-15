package com.gome.clover.zeromq;

import org.junit.Test;
import zmq.Ctx;
import zmq.SocketBase;
import zmq.ZMQ;

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
 * Date: 2014/12/25
 * Time: 21:23
 */
public class TestReqrepTcp {
    @Test
    public void testReqrepTcp() throws Exception
    {
        Ctx ctx = ZMQ.zmq_init(1);
        SocketBase sb = ZMQ.zmq_socket(ctx, ZMQ.ZMQ_REP);
        boolean rc = ZMQ.zmq_bind(sb, "tcp://127.0.0.1:7560");
        SocketBase sc = ZMQ.zmq_socket(ctx, ZMQ.ZMQ_REQ);
        rc = ZMQ.zmq_connect(sc, "tcp://127.0.0.1:7560");

        Helper.bounce(sb, sc);

        ZMQ.zmq_close(sc);
        ZMQ.zmq_close(sb);
        ZMQ.zmq_term(ctx);
    }
}
