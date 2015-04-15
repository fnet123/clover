package com.gome.clover.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
 * Module Desc:ZK Connect
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/28
 * Time: 16:07
 */

public class ZKConnect implements CuratorWatcher, CuratorListener, ConnectionStateListener {
    public CuratorFramework instance = null;
    public ArrayList listeners = new ArrayList();
    protected static Logger logger = LoggerFactory.getLogger(ZKConnect.class);

    public ZKConnect() {
        this.instance = ZKUtil.create();
    }

    public boolean start() {
        try {
            this.instance.getConnectionStateListenable().addListener(this);
            this.instance.getCuratorListenable().addListener(this);
            this.instance.start();
            this.instance.getZookeeperClient().blockUntilConnectedOrTimedOut();
            return true;
        } catch (Exception e) {
            logger.error("ZKConnect->> start() error ", e);
        }
        return false;
    }

    public void addServerListener(ServerListener serverListener) {
        this.listeners.add(serverListener);
    }

    public void addClientListener(ClientListener clientListener) {
        this.listeners.add(clientListener);
    }

    public void removeServerListener(ServerListener serverListener) {
        this.listeners.remove(serverListener);
    }

    public void removeClientListener(ClientListener clientListener) {
        this.listeners.remove(clientListener);
    }

    public boolean checkAlive() {
        return this.instance.getZookeeperClient().isConnected();
    }

    public String getData(String path) throws Exception {
        return ZKUtil.getData(this.instance, path, this);
    }

    public boolean exists(String path) {
        return ZKUtil.exists(this.instance, path, this);
    }

    public List<String> getChildrens(String path) throws Exception {
        if (!ZKUtil.exists(this.instance, path, this)) {
            return null;
        }
        return ZKUtil.getChilds(this.instance, path, this);
    }

    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        if (newState == ConnectionState.RECONNECTED) {
            logger.info("Zookeeper Reconnect [{}]", new String[]{client.toString()});
        } else if ((newState == ConnectionState.LOST) || (newState == ConnectionState.SUSPENDED)) {
            logger.info("Zookeeper Lost connection");
        }
    }

    private void fireEvents(WatchedEvent e) {
        for (int i = 0; i < this.listeners.size(); i++) {
            if (this.listeners.get(i) instanceof ServerListener) {
                ServerListener w = (ServerListener) this.listeners.get(i);
                w.process(e);
            } else if (this.listeners.get(i) instanceof ClientListener) {
                ClientListener w = (ClientListener) this.listeners.get(i);
                w.process(e);
            }
        }
    }

    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        if ((event == null) || (event.getPath() == null)) {
            return;
        }
        fireEvents(event.getWatchedEvent());
    }

    public void process(WatchedEvent event) {
        if ((event == null) || (event.getPath() == null)) {
            return;
        }
        fireEvents(event);
    }
}