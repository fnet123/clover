package com.gome.clover.common.zk;

import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.List2StringUtil;
import com.gome.clover.common.tools.StringUtil;
import com.mongodb.BasicDBObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.curator.framework.api.BackgroundPathable;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * Module Desc:clover
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/28
 * Time: 14:35
 */

public class ZKUtil {
    protected static Logger logger = LoggerFactory.getLogger(ZKUtil.class);
    private static List<ACL> acl = new ArrayList<ACL>();
    public static CuratorFramework create() {
        RetryNTimes retryPolicy = new RetryNTimes(5, 5000);
        String authString = CommonConstants.ZK_USER_NAME + ":"+CommonConstants.ZK_PASSWORD;
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(CommonConstants.ZK_CONNECT_STRING)
                .retryPolicy(retryPolicy).connectionTimeoutMs(CommonConstants.ZOO_KEEPER_TIMEOUT)
                .sessionTimeoutMs(CommonConstants.ZOO_KEEPER_TIMEOUT * 3).authorization("digest", authString.getBytes()).build();
        try {
            acl.clear();
            acl.add(new ACL(ZooDefs.Perms.ALL, new Id("digest",
                    DigestAuthenticationProvider.generateDigest(authString))));
            acl.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("ZKUtil-->>create() error,",e);
        }
        return client;
    }

    public static boolean exists(CuratorFramework client, String path, CuratorWatcher watcher) {
        try {
            if (watcher != null) {
                return ((BackgroundPathable)client.checkExists().usingWatcher(watcher)).forPath(path) != null;
            }
            return client.checkExists().forPath(path) != null;
        }
        catch (Exception e) {
            logger.error("ZKUtil-->>exists(CuratorFramework client, String path, CuratorWatcher watcher) error, ", e);
            System.err.println("ZKUtil-->>exists(CuratorFramework client, String path, CuratorWatcher watcher) error,"+e.getMessage());
        }return false;
    }

    public static boolean exists(CuratorFramework client, String path)
    {
        return exists(client, path, null);
    }

    public static void createPath(CuratorFramework client, String path, String content, CreateMode mode) {
        try {
            ((ACLBackgroundPathAndBytesable)client.create().creatingParentsIfNeeded().withMode(mode)).forPath(path, List2StringUtil.toBytes(content));
        } catch (Exception e) {
            logger.error("ZKUtil-->>createPath(CuratorFramework client, String path, String content, CreateMode mode) error,", e);
            System.err.println("ZKUtil-->>createPath(CuratorFramework client, String path, String content, CreateMode mode) error,"+e.getMessage());
        }
    }

    public static void setPath(CuratorFramework client, String path, String content, CreateMode mode) {
        try {
            if (client.checkExists().forPath(path) == null){
                ((ACLBackgroundPathAndBytesable)client.create().creatingParentsIfNeeded().withMode(mode)).forPath(path, List2StringUtil.toBytes(content));
            }else
                client.setData().forPath(path,List2StringUtil.toBytes(content));
        }
        catch (Exception e) {
            logger.error("ZKUtil-->>setPath(CuratorFramework client, String path, String content, CreateMode mode) error,", e);
            System.err.println("ZKUtil-->>setPath(CuratorFramework client, String path, String content, CreateMode mode) error," + e.getMessage());
        }
    }

    public static void updateServerTimestamp(long timestamp, String key) {
        if (timestamp == 0L)
            return;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp startime = new Timestamp(timestamp);
        String startDate = format.format(startime);
        setPath(ServerDict.self.getZK(), key, String.valueOf(timestamp), CreateMode.PERSISTENT);
        logger.info("ZKUtil-->> update key[{}]  timestamp[{}] ", new Object[] { key, startDate });
    }

    public static long readServerTimestamp(String key) {
        String c = getData(ServerDict.self.getZK(), key);
        if (StringUtil.isEmpty(c)) {
            return 0L;
        }
        return Long.parseLong(c);
    }

    public static String getData(CuratorFramework client, String path) {
        return getData(client, path, null);
    }

    public static String getData(CuratorFramework client, String path, CuratorWatcher watcher) {
        try {
            if (client.checkExists().forPath(path) == null) {
                return null;
            }
            if (watcher != null) {
                return List2StringUtil.toString((byte[])((BackgroundPathable)client.getData().usingWatcher(watcher)).forPath(path));
            }
            return List2StringUtil.toString((byte[])client.getData().forPath(path));
        }
        catch (Exception e) {
            logger.error("ZKUtil-->>getData(CuratorFramework client, String path, CuratorWatcher watcher)  error ", e);
            System.err.println("ZKUtil-->>getData(CuratorFramework client, String path, CuratorWatcher watcher)  error ,"+e.getMessage());
        }return null;
    }

    public static String createEphemeralSequential(CuratorFramework client, String path, byte[] payload)
    {
        try {
            return (String)((ACLBackgroundPathAndBytesable)client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)).forPath(path, payload);
        } catch (Exception e) {
            logger.error("ZKUtil-->>createEphemeralSequential", e);
        }return null;
    }

    public static void remove(CuratorFramework client, String path) {
        try {
            if (client.checkExists().forPath(path) == null) {
                logger.info("ZKUtil-->>remove(CuratorFramework client, String path) this Path not exists");
                System.err.println("ZKUtil-->>remove(CuratorFramework client, String path) this Path not exists");
                return;
            }
            client.delete().forPath(path);
        } catch (Exception e) {
            logger.error("ZKUtil-->>remove(CuratorFramework client, String path) error,", e);
            System.err.println("ZKUtil-->>remove(CuratorFramework client, String path) error ,"+e.getMessage());
        }
    }

    public static void delete(CuratorFramework client, String path) {
        try {
            client.delete().guaranteed().forPath(path);
        } catch (Exception e) {
            logger.error("ZKUtil-->>delete(CuratorFramework client, String path) error,", e);
            System.err.println("ZKUtil-->>delete(CuratorFramework client, String path) error,"+e.getMessage());
        }
    }

    public static List<String> getChilds(CuratorFramework client, String path) {
        return getChilds(client, path, null);
    }

    public static List<String> getChilds(CuratorFramework client, String path, CuratorWatcher watcher) {
        try {
            if (watcher != null) {
                return (List)((BackgroundPathable)client.getChildren().usingWatcher(watcher)).forPath(path);
            }
            return (List)client.getChildren().forPath(path);
        }
        catch (Exception e) {
            logger.error("ZKUtil-->>getChilds(CuratorFramework client, String path, CuratorWatcher watcher) error,", e);
            System.err.println("ZKUtil-->>getChilds(CuratorFramework client, String path, CuratorWatcher watcher) error,"+ e.getMessage());
        }return null;
    }

    public static String getDataByParameter(String path,String qKey,String qValue,String resultValue){
        CuratorFramework curatorFramework = ZKUtil.create();
        if (!curatorFramework.isStarted()) curatorFramework.start();
        List nodeList = ZKUtil.getChilds(curatorFramework,path);
        for (int i = 0; (nodeList != null) && (i < nodeList.size()); i++) {
            String id = (String) nodeList.get(i);
            String c = ZKUtil.getData(curatorFramework, path + "/" + id);
            if (c == null) {
                continue;
            }
            BasicDBObject record = (BasicDBObject) com.mongodb.util.JSON.parse(c);
            String tempValue = (String) record.get(qKey);
            if(tempValue.equals(qValue)){
                return (String) record.get(resultValue);
            }else {
                continue;
            }
    }
        return null;
    }

    public static BasicDBObject getDataByParameter(String path,String qKey,String qValue){
        CuratorFramework curatorFramework = ZKUtil.create();
        List nodeList = ZKUtil.getChilds(curatorFramework,path);
        for (int i = 0; (nodeList != null) && (i < nodeList.size()); i++) {
            String id = (String) nodeList.get(i);
            String c = ZKUtil.getData(curatorFramework, path + "/" + id);
            if (c == null) {
                continue;
            }
            BasicDBObject record = (BasicDBObject) com.mongodb.util.JSON.parse(c);
            String tempValue = (String) record.get(qKey);
            if(tempValue.equals(qValue)){
                return record;
            }else {
                continue;
            }
        }
        return null;
    }

    public static void main(String[] args)
    {
        ZKUtil.setPath(ClientDict.self.getZK(),"/test/wy/test1", "test2", CreateMode.EPHEMERAL);

    }
}