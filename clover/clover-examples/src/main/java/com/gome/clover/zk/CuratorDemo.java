package com.gome.clover.zk;

import com.gome.clover.common.zk.ZKUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.nio.charset.Charset;

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
 * Time: 15:13
 */
public class CuratorDemo {
    private CuratorFramework zkTools;
    public void curatorDemo() throws Exception{
        zkTools = ZKUtil.create();
        zkTools.start();

       /* zkTools.create()//创建一个路径
                .creatingParentsIfNeeded()//如果指定的节点的父节点不存在，递归创建父节点
                .withMode(CreateMode.PERSISTENT)//存储类型（临时的还是持久的）
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)//访问权限
                .forPath("zk/test");//创建的路径*/

      /*  zkTools.//对路径节点赋值
                setData().
                forPath("zk/test","hello world".getBytes(Charset.forName("utf-8")));*/

        byte[] buffer = zkTools.
                getData().
                usingWatcher(new ZKWatch("/clover/server")).forPath("/clover/server");
        //System.out.println(new String(buffer));
    }

    public static void main(String[] args) throws Exception {
        CuratorDemo test = new CuratorDemo();
        test.curatorDemo();

    }
    public class ZKWatch implements CuratorWatcher {
        private final String path;

        public String getPath() {
            return path;
        }
        public ZKWatch(String path) {
            this.path = path;
        }
        @Override
        public void process(WatchedEvent event) throws Exception {
            if(event.getType() == Watcher.Event.EventType.NodeDataChanged){
                byte[] data = zkTools.getData().forPath(path);
                System.out.println(path+":"+new String(data,Charset.forName("utf-8")));
            }
        }

    }
}
