package com.gome.clover.zeromq;

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
 * Date: 2014/12/24
 * Time: 18:18
 */
import com.gome.clover.common.tools.IpUtil;
import org.junit.Test;
import zmq.Ctx;
import zmq.Msg;
import zmq.SocketBase;
import zmq.ZMQ;

public class TestPubsubTcp {
    @Test
    public void testPubsubTcp() throws Exception
    {
        Ctx ctx = ZMQ.zmq_init(1);

        SocketBase sb = ZMQ.zmq_socket(ctx, ZMQ.ZMQ_PUB);
        boolean rc = ZMQ.zmq_bind(sb, "tcp://127.0.0.1:7660");

        SocketBase sc = ZMQ.zmq_socket(ctx, ZMQ.ZMQ_SUB);

        sc.setsockopt(ZMQ.ZMQ_SUBSCRIBE, IpUtil.getLocalIP());

        rc = ZMQ.zmq_connect(sc, "tcp://127.0.0.1:7660");

        ZMQ.zmq_sleep(2);

        sb.send(new Msg((IpUtil.getLocalIP()+" abc").getBytes(ZMQ.CHARSET)), 0);
        sb.send(new Msg("topix defg".getBytes(ZMQ.CHARSET)), 0);
        sb.send(new Msg("topic defgh".getBytes(ZMQ.CHARSET)), 0);


        Msg msg = sc.recv(0);
        System.err.println("msg1:"+new String(msg.data()));

        msg = sc.recv(0);
        System.err.println("msg2:"+new String(msg.data()));
        ZMQ.zmq_close(sc);
        ZMQ.zmq_close(sb);
        ZMQ.zmq_term(ctx);
    }
}
