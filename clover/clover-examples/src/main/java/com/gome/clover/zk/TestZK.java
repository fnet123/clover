package com.gome.clover.zk;

import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.PropertiesUtil;
import com.gome.clover.common.zeromq.ZeroMQPull;
import com.gome.clover.common.zk.ZKManager;
import org.junit.Test;

import java.util.List;

/**
 * Module Desc:
 * User: wangyue-ds6
 * Date: 2014/11/19
 * Time: 15:14
 */
public class TestZK {
    @Test
    public void testServerDelete(){
        try {
            ZKManager zkManager  = new ZKManager(PropertiesUtil.loadProperties());
            String pathStr = zkManager.getRootPath() + "/server/ip";
            zkManager.delete(pathStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testClientDelete(){
        try {
            ZKManager zkManager  = new ZKManager(PropertiesUtil.loadProperties());
            String pathStr = zkManager.getRootPath() + "/client";
            zkManager.delete(pathStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllData(){
        try {
            ZKManager zkManager  = new ZKManager(PropertiesUtil.loadProperties());
            String pathStr = zkManager.getRootPath() + "/" + CommonConstants.MODULE_TYPE_CLIENT ;
            List<String> childrenListStr = zkManager.getZooKeeper().getChildren(pathStr, false);
            System.err.println("childrenListStr:"+childrenListStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet() throws InterruptedException {
        synchronized (Object.class){
            Thread.sleep(100000);
            String addr = "tcp://*:" + CommonConstants.ZMQ_SERVER_PORT;
            ZeroMQPull.INSTANCE.recv(addr);
        }

        System.err.println("zkString:"+CommonConstants.ZK_CONNECT_STRING);

    }

}
