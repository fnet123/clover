package com.gome.clover.common.zeromq;

import com.gome.clover.common.compress.CompressUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
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
 * Module Desc:ZeroMQ Push
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public enum ZeroMQPush {
    INSTANCE;
    public boolean send(String addr,String msg){
        try{
            ZMQ.Context context = ZMQ.context(1);
            ZMQ.Socket push  = context.socket(ZMQ.PUSH);
            push.connect(addr);
            boolean retValue = push.send(CompressUtil.compress(msg.getBytes()),ZMQ.NOBLOCK);
            push.close();
            context.term();
            return retValue;
            }catch (Exception e){
                System.err.println(""+e);
                return false;
            }
    }

    public static void main(String args[]){
        String addr="tcp://"+ IpUtil.getLocalIP()+":"+ CommonConstants.ZMQ_SERVER_PORT;
        String msg="你好";
        ZeroMQPush.INSTANCE.send(addr,msg);
    }
}
