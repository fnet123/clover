package com.gome.clover.common.zk;

import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.StringUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Time: 16:08
 */

public class ServerListener implements Watcher  {
    protected static Logger logger = LoggerFactory.getLogger(ServerListener.class);
    protected String prefixPath = "";
    protected ServerNodes serverNodes = new ServerNodes();
    protected ZKConnect zkConnect = null;
    protected Integer LOCK = Integer.valueOf(0);

    public ServerListener(String prefixPath, ZKConnect zkConnect) {
        this.prefixPath = prefixPath;
        this.zkConnect = zkConnect;
    }

    public void removeRecord(String id) {
        this.serverNodes.remove(id);
    }


    public BasicDBObject addRecord(String id) {
        if (!this.zkConnect.checkAlive())
            return null;
        try {
            String c = this.zkConnect.getData(this.prefixPath + "/" + id);
            if (c == null) {
                return null;
            }
            BasicDBObject record = (BasicDBObject) JSON.parse(c);
            this.serverNodes.add(record);
            return record;
        } catch (Exception e) {
            logger.error("ServerNodeListener-->>addRecord(" + id + ") error ", e);
            return null;
        }
    }

    public void reload() {
        if (!this.zkConnect.checkAlive()) {
            return;
        }
        if (!this.zkConnect.exists(this.prefixPath))
            return;
        try {
            String timeStamp = DateUtil.currentDateTime();
            List items = this.zkConnect.getChildrens(this.prefixPath);
            for (int i = 0; (items != null) && (i < items.size()); i++) {
                String id = (String) items.get(i);
                String c = this.zkConnect.getData(this.prefixPath + "/" + id);
                if (c == null) {
                    continue;
                }
                BasicDBObject record = (BasicDBObject) JSON.parse(c);
                record.put("timeStamp", timeStamp);
                this.serverNodes.add(record);
            }
            int index = 0;
            while (index < this.serverNodes.records.size()) {
                BasicDBObject record = (BasicDBObject) this.serverNodes.records.get(index);
                if (record.getString("timeStamp").equalsIgnoreCase(timeStamp)) {
                    index++;
                } else if (!this.serverNodes.remove(record.getString("id"))) {
                    index++;
                }
            }
        } catch (Exception e) {
            logger.error("ServerListener-->>reload() error ", e);
        }
    }

    public void process(WatchedEvent event) {
        if (event == null) {
            return;
        }
        String path = event.getPath();
        if ((path == null) || (!path.startsWith(this.prefixPath))) {
            return;
        }
        synchronized (this.LOCK) {
            try {
                String[] values = StringUtil.split(path, '/');
                String id = values.length >= 2 ? values[1] : "";
                if (event.getType() == Watcher.Event.EventType.NodeCreated) {
                    addRecord(id);
                } else if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                    removeRecord(id);
                } else if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
                    addRecord(id);
                } else if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    reload();
                }
            } catch (Exception e) {
                logger.error("ServerListener-->>process(" + com.alibaba.fastjson.JSON.toJSONString(event) + ") error.", e);
            }
        }
    }
}