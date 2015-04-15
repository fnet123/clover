package com.gome.clover.common.zk;

import com.gome.clover.common.hash.HashTimes;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.StringUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
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
 * Module Desc:Server Nodes
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/28
 * Time: 16:09
 */

public class ServerNodes extends CommonNodes {
    protected static Logger logger = LoggerFactory.getLogger(ServerNodes.class);
    protected BasicDBList records = new BasicDBList();

    public BasicDBObject hashServer(String hashKey) {
        if ((records == null) || (records.size() == 0)) {
            return null;
        }
        if (StringUtil.isEmpty(hashKey)) {
            hashKey = allocateNo();
        }
        int index = HashTimes.use33(hashKey) % records.size();
        return (BasicDBObject) records.get(index);
    }

    /**
     * hashServer升级版本(4S==For Super)
     *
     * @param hashKey
     * @return
     */
    public Map hashServer4S(String hashKey) {
        Map map = new HashMap();
        if ((records == null) || (records.size() == 0)) {
            map.put(CommonConstants.SUCCESS, false);
            map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_101);
            return map;
        }
        if (StringUtil.isEmpty(hashKey)) {
            hashKey = allocateNo();
        }
        int index = HashTimes.use33(hashKey) % records.size();

        map.put(CommonConstants.SUCCESS, true);
        map.put(CommonConstants.SERVER_JOB_INFO, records.get(index));
        return map;
    }

    public BasicDBObject hashServerByFixedServerIps(String[] fixedServerIps) {
        if ((records == null) || (records.size() == 0)) {
            return null;
        }
        int index = -1;
        for (String fixedServerIp : fixedServerIps) {
            index = findIndexByIp(fixedServerIp, records);
            if (index == -1) continue;
        }
        if (index == -1) {
            return null;
        }
        return (BasicDBObject) records.get(index);
    }

    /**
     * hashServerByFixedServerIps升级版本(4S==For Super)
     *
     * @param fixedServerIps
     * @return
     */
    public Map hashServer4SByFixedServerIps(String[] fixedServerIps) {
        Map map = new HashMap();
        if ((records == null) || (records.size() == 0)) {
            map.put(CommonConstants.SUCCESS, false);
            map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_101);
            return map;
        }
        int index = -1;
        for (String fixedServerIp : fixedServerIps) {
            index = findIndexByIp(fixedServerIp, records);
            if (index == -1) continue;
        }
        if (index == -1) {
            map.put(CommonConstants.SUCCESS, false);
            map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_101);
            return map;
        }
        map.put(CommonConstants.SUCCESS, true);
        map.put(CommonConstants.SERVER_JOB_INFO, records.get(index));
        return map;
    }

    public BasicDBObject hashServerBySystemCapacity() {
        int itemLen;
        if ((records == null) || ((itemLen = records.size()) == 0)) {
            return null;
        } else if (1 == itemLen) {
            BasicDBObject item = (BasicDBObject) records.get(0);
            double memRatio = Double.valueOf(String.valueOf(item.get(CommonConstants.MEM_RATIO)));
            if (memRatio > CommonConstants.MAX_MEM_RATIO) {
                logger.error("ServerNodes-->>hashServerBySystemCapacity() the memRatio(" + memRatio + ") over max mem ratio(" + CommonConstants.MAX_MEM_RATIO + ") ");
                return null;
            } else {
                return item;
            }
        } else if (itemLen > 1) {
            BasicDBObject tempItem = (BasicDBObject) records.get(0);
            int minMemRatioIndex = 0;
            double minMemRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.MEM_RATIO)));
            double minCpuRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.CPU_RATIO)));
            int minTotalThreadValue = Integer.valueOf(String.valueOf(tempItem.get(CommonConstants.TOTAL_THREAD)));
            double minTotalValue = minMemRatioValue + minCpuRatioValue + minTotalThreadValue;
            for (int i = 1; i < itemLen; i++) {
                tempItem = (BasicDBObject) records.get(i);
                double tempMemRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.MEM_RATIO)));
                double tempCpuRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.CPU_RATIO)));
                int tempTotalThreadValue = Integer.valueOf(String.valueOf(tempItem.get(CommonConstants.TOTAL_THREAD)));
                double tempTotalValue = tempMemRatioValue + tempCpuRatioValue + tempTotalThreadValue;
                if (tempTotalValue < minTotalValue) {
                    minMemRatioValue = tempMemRatioValue;
                    minCpuRatioValue = tempCpuRatioValue;
                    minTotalThreadValue = tempTotalThreadValue;
                    minTotalValue = minMemRatioValue + minCpuRatioValue + minTotalThreadValue;
                    minMemRatioIndex = i;
                }
            }
            if (minMemRatioValue > CommonConstants.MAX_MEM_RATIO || minCpuRatioValue > CommonConstants.MAX_CPU_RATIO) {
                logger.error("ServerNodes-->>hashServerBySystemCapacity() the memRatio(" + minMemRatioValue +
                        ") over max mem ratio(" + CommonConstants.MAX_MEM_RATIO + ") " +
                        "or the cpuRatio(" + minCpuRatioValue + ") over max cpu ration (" + CommonConstants.MAX_CPU_RATIO + ") ");
                return null;
            } else {
                return (BasicDBObject) records.get(minMemRatioIndex);
            }
        }
        return null;
    }

    /**
     * hashServerBySystemCapacity升级版本(4S==For Super)
     *
     * @return
     */
    public Map hashServer4SBySystemCapacity() {
        Map map = new HashMap();
        int itemLen;
        if ((records == null) || ((itemLen = records.size()) == 0)) {
            map.put(CommonConstants.SUCCESS, false);
            map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_101);
            return map;
        } else if (1 == itemLen) {
            BasicDBObject item = (BasicDBObject) records.get(0);
            double memRatio = Double.valueOf(String.valueOf(item.get(CommonConstants.MEM_RATIO)));
            double cpuRatio = Double.valueOf(String.valueOf(item.get(CommonConstants.CPU_RATIO)));
            if (memRatio > CommonConstants.MAX_MEM_RATIO || cpuRatio > CommonConstants.MAX_CPU_RATIO) {
                logger.error("ClientNodes-->>hashServer4SBySystemCapacity() the memRatio(" + memRatio + ")" +
                        " over max mem ratio(" + CommonConstants.MAX_MEM_RATIO + ") or the cpuRatio(" + cpuRatio + ")" +
                        " over max cpu ratio(" + CommonConstants.MAX_CPU_RATIO + ") ");
                map.put(CommonConstants.SUCCESS, false);
                map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_102);
                map.put(CommonConstants.MEM_RATIO, memRatio);
                map.put(CommonConstants.CPU_RATIO, cpuRatio);
                return map;
            } else {
                map.put(CommonConstants.SUCCESS, true);
                map.put(CommonConstants.SERVER_JOB_INFO, item);
                return map;
            }
        } else if (itemLen > 1) {
            BasicDBObject tempItem = (BasicDBObject) records.get(0);
            int minMemRatioIndex = 0;
            double minMemRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.MEM_RATIO)));
            double minCpuRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.CPU_RATIO)));
            int minTotalThreadValue = Integer.valueOf(String.valueOf(tempItem.get(CommonConstants.TOTAL_THREAD)));
            double minTotalValue = minMemRatioValue + minCpuRatioValue + minTotalThreadValue;
            for (int i = 1; i < itemLen; i++) {
                tempItem = (BasicDBObject) records.get(i);
                double tempMemRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.MEM_RATIO)));
                double tempCpuRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.CPU_RATIO)));
                int tempTotalThreadValue = Integer.valueOf(String.valueOf(tempItem.get(CommonConstants.TOTAL_THREAD)));
                double tempTotalValue = tempMemRatioValue + tempCpuRatioValue + tempTotalThreadValue;
                if (tempTotalValue < minTotalValue) {
                    minMemRatioValue = tempMemRatioValue;
                    minCpuRatioValue = tempCpuRatioValue;
                    minTotalThreadValue = tempTotalThreadValue;
                    minTotalValue = minMemRatioValue + minCpuRatioValue + minTotalThreadValue;
                    minMemRatioIndex = i;
                }
            }
            if (minMemRatioValue > CommonConstants.MAX_MEM_RATIO || minCpuRatioValue > CommonConstants.MAX_CPU_RATIO) {
                logger.error("ClientNodes-->>hashServer4SBySystemCapacity() " +
                        "the memRatio(" + minMemRatioValue + ") over max mem ratio(" + CommonConstants.MAX_MEM_RATIO + ")" +
                        "or the cpuRatio(" + minCpuRatioValue + ") over max cpu ration (" + CommonConstants.MAX_CPU_RATIO + ") ");
                map.put(CommonConstants.SUCCESS, false);
                map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_102);
                map.put(CommonConstants.MEM_RATIO, minMemRatioValue);
                map.put(CommonConstants.CPU_RATIO, minCpuRatioValue);
                return map;
            } else {
                map.put(CommonConstants.SUCCESS, true);
                map.put(CommonConstants.SERVER_JOB_INFO, records.get(minMemRatioIndex));
                return map;
            }
        }
        map.put(CommonConstants.SUCCESS, false);
        map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_103);
        return map;
    }

    public synchronized void add(BasicDBObject record) {
        String id = record.getString(CommonConstants.ID);
        int index = findIndexById(id, this.records);
        if (index == -1)
            this.records.add(record);
        else {
            this.records.set(index, record);
        }
    }

    public synchronized boolean remove(String id) {
        try {
            int index = findIndexById(id, this.records);
            return null != this.records.remove(index);
        }catch (Exception e){
            return  false;
        }
    }
}