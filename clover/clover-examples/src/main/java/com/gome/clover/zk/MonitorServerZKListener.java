package com.gome.clover.zk;

import com.gome.clover.common.zk.ZKUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentSkipListSet;

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
 * Date: 2014/12/19
 * Time: 12:11
 */
public class MonitorServerZKListener implements CuratorListener,ConnectionStateListener {
    protected static Logger logger = LoggerFactory.getLogger(MonitorServerZKListener.class);
    private CuratorFramework zkTools;
    private ConcurrentSkipListSet watchers = new ConcurrentSkipListSet();
    private static Charset charset = Charset.forName("utf-8");
    private  String prefixPath;

    public MonitorServerZKListener(String prefixPath) {
        this.prefixPath= prefixPath;
        zkTools = ZKUtil.create();
        zkTools.start();
        CuratorWatcher watcher = new ZKWatchRegister(prefixPath);    //创建一个register watcher
        addReconnectionWatcher(prefixPath, ZookeeperWatcherType.CREATE_ON_NO_EXITS,watcher);
        this.zkTools.getConnectionStateListenable().addListener(this);
        this.zkTools.getCuratorListenable().addListener(this);
        byte[] data = new byte[0];
        try {
           zkTools.getData().usingWatcher(watcher);
            //data = zkTools.getData().usingWatcher(watcher).forPath(prefixPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("get path form zk : "+prefixPath+":"+new String(data,charset));
    }

    public void addReconnectionWatcher(final String path,final ZookeeperWatcherType watcherType,final CuratorWatcher watcher){
        synchronized (this) {
            if(!watchers.contains(watcher.toString()))//不要添加重复的监听事件
            {
                watchers.add(watcher.toString());
                System.out.println("add new watcher " + watcher);
                zkTools.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                    @Override
                    public void stateChanged(CuratorFramework client, ConnectionState newState) {
                        System.out.println(newState);
                        if(newState == ConnectionState.LOST){//处理session过期
                            try{
                                if(watcherType == ZookeeperWatcherType.EXITS){
                                    zkTools.checkExists().usingWatcher(watcher).forPath(path);
                                }else if(watcherType == ZookeeperWatcherType.GET_CHILDREN){
                                    zkTools.getChildren().usingWatcher(watcher).forPath(path);
                                }else if(watcherType == ZookeeperWatcherType.GET_DATA){
                                    zkTools.getData().usingWatcher(watcher).forPath(path);
                                }else if(watcherType == ZookeeperWatcherType.CREATE_ON_NO_EXITS){
                                    //ephemeral类型的节点session过期了，需要重新创建节点，并且注册监听事件，之后监听事件中，
                                    //会处理create事件，将路径值恢复到先前状态
                                    Stat stat = zkTools.checkExists().usingWatcher(watcher).forPath(path);
                                    if(stat == null){
                                        System.err.println("to create");
                                        zkTools.create()
                                                .creatingParentsIfNeeded()
                                                .withMode(CreateMode.EPHEMERAL)
                                                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                                                .forPath(path);
                                    }
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {

    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {

    }

    public class ZKWatchRegister implements CuratorWatcher{
        private final String path;
        private byte[] value;
        public String getPath() {
            return path;
        }
        public ZKWatchRegister(String path) {
            this.path = path;
        }
        @Override
        public void process(WatchedEvent event) throws Exception {
            System.out.println(event.getType());
            if(event.getType() == Watcher.Event.EventType.NodeDataChanged){
                //节点数据改变了，需要记录下来，以便session过期后，能够恢复到先前的数据状态
                byte[] data = zkTools.
                        getData().
                        usingWatcher(this).forPath(path);
                value = data;
                System.out.println(path+":"+new String(data,charset));
            }else if(event.getType() == Watcher.Event.EventType.NodeDeleted){
                //节点被删除了，需要创建新的节点
                System.out.println(path + ":" + path +" has been deleted.");
                Stat stat = zkTools.checkExists().usingWatcher(this).forPath(path);
                if(stat == null){
                    zkTools.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.EPHEMERAL)
                            .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                            .forPath(path);
                }
            }else if(event.getType() == Watcher.Event.EventType.NodeCreated){
                //节点被创建时，需要添加监听事件（创建可能是由于session过期后，curator的状态监听部分触发的）
                System.out.println(path + ":" +" has been created!" + "the current data is " + new String(value));
                zkTools.setData().forPath(path, value);
                zkTools.getData().usingWatcher(this).forPath(path);
            }
        }
    }
    public enum ZookeeperWatcherType{
        GET_DATA,GET_CHILDREN,EXITS,CREATE_ON_NO_EXITS
    }
}
