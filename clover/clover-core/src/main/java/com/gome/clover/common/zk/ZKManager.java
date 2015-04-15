package com.gome.clover.common.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

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
 * Module Desc:ZKManager
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class ZKManager {
    private static transient Logger logger = LoggerFactory.getLogger(ZKManager.class);
    private ZooKeeper zk;
    private List<ACL> acl = new ArrayList<ACL>();
    private Properties properties;
    public enum keys {
        zkConnectString, rootPath, userName, password, zkSessionTimeout
    }
    public ZKManager(Properties properties) throws Exception{
        this.properties = properties;
        this.connect();
    }

    /**
     * 重连zookeeper
     * @throws Exception
     */
    public synchronized void  reConnection() throws Exception{
        if (this.zk != null) {
            this.zk.close();
            this.zk = null;
            this.connect() ;
        }
    }

    private void connect() throws Exception {
        CountDownLatch connectionLatch = new CountDownLatch(1);
        createZookeeper(connectionLatch);
        connectionLatch.await();
    }

    private void createZookeeper(final CountDownLatch connectionLatch) throws Exception {
        zk = new ZooKeeper(this.properties.getProperty(keys.zkConnectString
                .toString()), Integer.parseInt(this.properties
                .getProperty(keys.zkSessionTimeout.toString())),
                new Watcher() {
                    public void process(WatchedEvent event) {
                        sessionEvent(connectionLatch, event);
                    }
                });
        String authString = this.properties.getProperty(keys.userName.toString())
                + ":"+ this.properties.getProperty(keys.password.toString());
        zk.addAuthInfo("digest", authString.getBytes());
        acl.clear();
        acl.add(new ACL(ZooDefs.Perms.ALL, new Id("digest",
                DigestAuthenticationProvider.generateDigest(authString))));
        acl.add(new ACL(ZooDefs.Perms.READ, Ids.ANYONE_ID_UNSAFE));
    }

    private void sessionEvent(CountDownLatch connectionLatch, WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            logger.info("收到ZK连接成功事件！");
            connectionLatch.countDown();
        } else if (event.getState() == KeeperState.Expired) {
            logger.error("会话超时，等待重新建立ZK连接...");
            try {
                reConnection();
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        } // Disconnected：Zookeeper会自动处理Disconnected状态重连
    }

    public void close() throws InterruptedException {
        logger.info("关闭zookeeper连接");
        this.zk.close();
    }

    public static Properties createProperties(){
        Properties result = new Properties();
        result.setProperty(keys.zkConnectString.toString(),"localhost:2181");
        result.setProperty(keys.rootPath.toString(),"/cloud-schedule/jobName");
        result.setProperty(keys.userName.toString(),"ScheduleAdmin");
        result.setProperty(keys.password.toString(),"password");
        result.setProperty(keys.zkSessionTimeout.toString(),"60000");
        return result;
    }
    public String getRootPath(){
        return this.properties.getProperty(keys.rootPath.toString());
    }
    public String getConnectStr(){
        return this.properties.getProperty(keys.zkConnectString.toString());
    }
    public boolean checkZookeeperState() throws Exception{
        return zk != null && zk.getState() == States.CONNECTED;
    }

    public boolean isExists(String path) throws Exception{
        return getZooKeeper().exists(path, false) != null;
    }
    public void setData(String path,String data) throws Exception{
        if(!isExists(path)){
            createPath(getZooKeeper(), path, CreateMode.EPHEMERAL,acl);
        }
        getZooKeeper().setData(path,data.getBytes(),-1);
    }
    public String getData(String path)throws Exception{
        if(!isExists(path)){
            return "";
        }
        return new String(getZooKeeper().getData(path, false, null));
    }
    public  void createPath(ZooKeeper zk, String path,CreateMode createMode, List<ACL> acl) throws Exception {
        String[] list = path.split("/");
        String zkPath = "";
        for (String str : list) {
            if (str.equals("") == false) {
                zkPath = zkPath + "/" + str;
                if (zk.exists(zkPath, false) == null) {
                    zk.create(zkPath, null, acl, createMode);
                }
            }
        }
    }

    public void delete(String path)throws Exception {
        getZooKeeper().delete(path,-1);
    }
    public List<ACL> getAcl() {
        return acl;
    }
    public ZooKeeper getZooKeeper() throws Exception {
        if(this.checkZookeeperState()==false){
            reConnection();
        }
        return this.zk;
    }

}
