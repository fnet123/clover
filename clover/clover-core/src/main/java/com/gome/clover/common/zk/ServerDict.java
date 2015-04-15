package com.gome.clover.common.zk;

import com.gome.clover.common.tools.CommonConstants;
import com.mongodb.BasicDBObject;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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
 * Module Desc:Server Dict
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/28
 * Time: 16:00
 */

public class ServerDict {
    public static ServerDict self = null;
    protected static Logger logger = LoggerFactory.getLogger(ServerDict.class);
    protected ZKConnect zkConnect = null;
    public ServerListener serverListener = null;

    static {
        self = new ServerDict();
    }

    public ServerDict() {
        init();
    }

    public boolean init() {
        logger.info("ServerDict init zookeeper...");
        this.zkConnect = new ZKConnect();
        if ((this.zkConnect == null) || (!this.zkConnect.start())) {
            return false;
        }
        this.serverListener = new ServerListener(CommonConstants.ZK_ROOT_PATH + "/server", this.zkConnect);
        this.zkConnect.addServerListener(this.serverListener);
        this.serverListener.reload();
        return true;
    }

    public CuratorFramework getZK() {
        return this.zkConnect.instance;
    }

    public BasicDBObject hashServer(String hashKey) {
        return this.serverListener.serverNodes.hashServer(hashKey);
    }

    /**
     * hashServer升级版本(4S==For Super)
     * @param hashKey
     * @return
     */
    public Map hashServer4S(String hashKey) {
        return this.serverListener.serverNodes.hashServer4S(hashKey);
    }

    public BasicDBObject hashServerByFixedServerIps(String[] fixedServerIps){
        return this.serverListener.serverNodes.hashServerByFixedServerIps(fixedServerIps);
    }

    /**
     * hashServerByFixedServerIps升级版本(4S==For Super)
     * @param fixedServerIps
     * @return
     */
    public Map hashServer4SByFixedServerIps(String[] fixedServerIps){
        return this.serverListener.serverNodes.hashServer4SByFixedServerIps(fixedServerIps);
    }
    public BasicDBObject hashServerBySystemCapacity() {
        return this.serverListener.serverNodes.hashServerBySystemCapacity();
    }

    /**
     * hashClientBySystemCapacity升级版本(4S==For Super)
     * @return
     */
    public Map hashServer4SBySystemCapacity() {
        return this.serverListener.serverNodes.hashServer4SBySystemCapacity();
    }

}
