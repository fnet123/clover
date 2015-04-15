package com.gome.clover.monitor;

import com.gome.clover.common.hash.HashTimes;
import com.gome.clover.common.mongodb.DBTableInfo;
import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.SendMailUtil;
import com.gome.clover.common.tools.StringUtil;
import com.gome.clover.common.zeromq.AsyncSendMsg;
import com.gome.clover.common.zeromq.ZeroMQEntity;
import com.gome.clover.common.zk.CommonNodes;
import com.gome.clover.common.zk.ZKUtil;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.TimerTask;

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
 * Module Desc: Server Timer Task(所有server不可用，报警；集群部署多台集群，某台不可用，将数据转移到另一台集群)
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */
public class ServerTimerTask extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(ServerTimerTask.class);
    private BasicDBList recordDBList;
    private BasicDBList serverNodeDBList;

    public ServerTimerTask(BasicDBList recordDBList) {
        this.recordDBList = recordDBList;
    }

    @Override
    public void run() {
        CuratorFramework curatorFramework = ZKUtil.create();
        if (!curatorFramework.isStarted()) curatorFramework.start();
        String serverPathStr = CommonConstants.ZK_ROOT_PATH + "/" + CommonConstants.MODULE_TYPE_SERVER;
        List serverNodeList = ZKUtil.getChilds(curatorFramework, serverPathStr);
        if (null != serverNodeList && serverNodeList.size() > 0) {
            addOrUpdateRecordList(curatorFramework, serverPathStr, serverNodeList);
            BasicDBList needRemoveNodeList = new BasicDBList();
            int index = 0;
            filterNeedRemoveNodeList(needRemoveNodeList, index);
            index = 0;
            transferServerData(needRemoveNodeList, index);
        } else {
            int index = 0;
            while (index < this.recordDBList.size()) {
                BasicDBObject record = (BasicDBObject) this.recordDBList.get(index);
                String msg = " Server of Ip:" + record.get("ip") + " not alive,Please deal with as quickly as possible";
                //报警通知相关人员
                SendMailUtil.sendDefaultMail(msg, msg);
                index++;
            }
        }
    }

    private void transferServerData(BasicDBList needRemoveNodeList, int index) {
        while (index < needRemoveNodeList.size()) {
            BasicDBObject needRemoveNode = (BasicDBObject) needRemoveNodeList.get(index);
            String removeNodeIp = (String) needRemoveNode.get("ip");
            if (!StringUtil.isEmpty(removeNodeIp)) {
                DBCollection dbCollection = MongoDBUtil.INSTANCE.getCollection(DBTableInfo.TBL_CLOVER_JOB);
                DBObject condition = new BasicDBObject();
                condition.put(DBTableInfo.COL_CLIENT_IP, removeNodeIp);
                condition.put(DBTableInfo.COL_JOB_TYPE, CommonConstants.JOB_TYPE_REMOTE);
                DBCursor cursorDocMap = dbCollection.find(condition);
                while (cursorDocMap.hasNext()) {
                    DBObject tempDBObject = cursorDocMap.next();
                    String hashKey = CommonNodes.allocateNo();
                    int tempIndex = HashTimes.use33(hashKey) % recordDBList.size();
                    BasicDBObject tempRecord = (BasicDBObject) recordDBList.get(tempIndex);
                    String newNodeIp = (String) tempRecord.get("ip");
                    if (MongoDBUtil.INSTANCE.update(new BasicDBObject(DBTableInfo.COL_ID, tempDBObject.get(DBTableInfo.COL_ID)),
                            new BasicDBObject(DBTableInfo.COL_CLIENT_IP, newNodeIp), DBTableInfo.TBL_CLOVER_JOB)) { //发消息到SERVER
                        String newNodePort = (String) tempRecord.get("port");
                        BasicDBObject basicDBObject = new BasicDBObject();
                        basicDBObject.put("action", "reloadDB");
                        basicDBObject.put("ip", newNodeIp);
                        String msg = JSON.serialize(basicDBObject);
                        ZeroMQEntity zeroMQEntity = new ZeroMQEntity(CommonConstants.MODULE_TYPE_SERVER_WITH_MONITOR, newNodeIp, null, msg);
                        //AsyncSendMsg.send(newNodeIp, newNodePort, com.alibaba.fastjson.JSON.toJSONString(zeroMQEntity));
                    }
                }
            }
            index++;
        }
    }

    private void filterNeedRemoveNodeList(BasicDBList needRemoveNodeList, int index) {
        while (index < this.recordDBList.size()) {
            BasicDBObject tempRecord = (BasicDBObject) this.recordDBList.get(index);
            if (!serverNodeDBList.contains(tempRecord)) {
                needRemoveNodeList.add(tempRecord);
                remove((String) tempRecord.get("id"), this.recordDBList);
            }
            index++;
        }
        serverNodeDBList.clear();
    }

    private void addOrUpdateRecordList(CuratorFramework curatorFramework, String serverPathStr, List serverNodeList) {
        for (int i = 0; (serverNodeList != null) && (i < serverNodeList.size()); i++) {
            String id = (String) serverNodeList.get(i);
            String c = ZKUtil.getData(curatorFramework, serverPathStr + "/" + id);
            if (c == null) {
                continue;
            }
            //c 是否在recordList中存在，如果不存在，那就认为c节点信息已不存了，此刻要将c节点中所有job同步到另一台机器中
            BasicDBObject record = (BasicDBObject) JSON.parse(c);
            add(record, recordDBList); //add or update
            serverNodeDBList = new BasicDBList();
            add(record, serverNodeDBList); //add or update
        }
    }

    public synchronized void add(BasicDBObject record, BasicDBList dBList) {
        String id = record.getString("id");
        int index = CommonNodes.findIndexById(id, dBList);
        if (index == -1)
            dBList.add(record);
        else {
            dBList.set(index, record);
        }
    }

    public synchronized boolean remove(String id, BasicDBList dBList) {
        int index = CommonNodes.findIndexById(id, dBList);
        return null != dBList.remove(index);
    }

}
