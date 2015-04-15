package com.gome.clover.common.zk;

import com.gome.clover.common.hash.HashTimes;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.StringUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
 * Module Desc:Client Nodes
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/28
 * Time: 21:40
 */
public class ClientNodes extends CommonNodes {
    protected static Logger logger = LoggerFactory.getLogger(ServerNodes.class);
    protected HashMap<String, BasicDBObject> recordsById = new HashMap();
    protected HashMap<String, BasicDBList> recordsByJobClass = new HashMap();
    protected BasicDBList records = new BasicDBList();
    protected HashSet<String> jobClassSet = new HashSet();

    public BasicDBObject hashClient(String jobClass, String hashKey) {
        BasicDBList items = getRecordsByJobClass(jobClass);
        if ((items == null) || (items.size() == 0)) {
            return null;
        }
        if (StringUtil.isEmpty(hashKey)) {
            hashKey = allocateNo();
        }
        int index = HashTimes.use33(hashKey) % items.size();
        return (BasicDBObject) items.get(index);
    }

    /**
     * hashClient升级版本(4S==For Super)
     * @param jobClass
     * @param hashKey
     * @return
     */
    public Map hashClient4S(String jobClass, String hashKey) {
        Map map = new HashMap();
        BasicDBList items = getRecordsByJobClass(jobClass);
        if ((items == null) || (items.size() == 0)) {
            map.put(CommonConstants.SUCCESS, false);
            map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_101);
            return map;
        }
        if (StringUtil.isEmpty(hashKey)) {
            hashKey = allocateNo();
        }
        int index = HashTimes.use33(hashKey) % items.size();
        map.put(CommonConstants.SUCCESS, true);
        map.put(CommonConstants.CLIENT_JOB_INFO, items.get(index));
        return map;
    }

    public BasicDBObject hashClientByFixedClientIps(String jobClass, String[] fixedClientIps) {
        BasicDBList items = getRecordsByJobClass(jobClass);
        if ((items == null) || (items.size() == 0)) {
            return null;
        }
        int index = -1;
        for (String fixedClientIp : fixedClientIps) {
            index = findIndexByIp(fixedClientIp, items);
            if (index == -1) continue;
        }
        if (index == -1) {
            return null;
        }
        return (BasicDBObject) items.get(index);
    }

    /**
     * hashClientByFixedClientIps升级版本(4S==For Super)
     * @param jobClass
     * @param fixedClientIps
     * @return
     */
    public Map hashClient4SByFixedClientIps(String jobClass, String[] fixedClientIps) {
        Map map = new HashMap();
        BasicDBList items = getRecordsByJobClass(jobClass);
        if ((items == null) || (items.size() == 0)) {
            map.put(CommonConstants.SUCCESS, false);
            map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_101);
        }
        int index = -1;
        for (String fixedClientIp : fixedClientIps) {
            index = findIndexByIp(fixedClientIp, items);
            if (index == -1) continue;
        }
        if (index == -1) {
            map.put(CommonConstants.SUCCESS, false);
            map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_101);
        }
        map.put(CommonConstants.SUCCESS, true);
        map.put(CommonConstants.CLIENT_JOB_INFO, items.get(index));
        return map;
    }

    public BasicDBObject hashClientBySystemCapacity(String jobClass) {
        BasicDBList items = getRecordsByJobClass(jobClass);
        int itemLen;
        if ((items == null) || ((itemLen = items.size()) == 0)) {
            return null;
        } else if (1 == itemLen) {
            BasicDBObject item = (BasicDBObject) items.get(0);
            double memRatio = Double.valueOf(String.valueOf(item.get(CommonConstants.MEM_RATIO)));
            double cpuRatio = Double.valueOf(String.valueOf(item.get(CommonConstants.CPU_RATIO)));
            if (memRatio > CommonConstants.MAX_MEM_RATIO || cpuRatio > CommonConstants.MAX_CPU_RATIO  ) {
                logger.error("ClientNodes-->>hashClientBySystemCapacity(" + jobClass + ") the memRatio(" + memRatio + ")" +
                        " over max mem ratio("+CommonConstants.MAX_MEM_RATIO+") or the cpuRatio("+cpuRatio+") " +
                        "over max cpu ratio("+CommonConstants.MAX_CPU_RATIO+") ");
                return null;
            } else {
                return item;
            }
        } else if (itemLen > 1) {
            BasicDBObject tempItem = (BasicDBObject) items.get(0);
            int minMemRatioIndex = 0;
            double minMemRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.MEM_RATIO)));
            double minCpuRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.CPU_RATIO)));
            int minTotalThreadValue = Integer.valueOf(String.valueOf(tempItem.get(CommonConstants.TOTAL_THREAD)));
            double minTotalValue = minMemRatioValue + minCpuRatioValue + minTotalThreadValue;
            for (int i = 1; i < itemLen; i++) {
                tempItem = (BasicDBObject) items.get(i);
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
                logger.error("ClientNodes-->>hashClientBySystemCapacity(" + jobClass + ") " +
                        "the memRatio(" + minMemRatioValue + ") over max mem ratio(" + CommonConstants.MAX_MEM_RATIO + ")" +
                        "or the cpuRatio("+minCpuRatioValue+") over max cpu ration ("+CommonConstants.MAX_CPU_RATIO+") ");
                return null;
            } else {
                return (BasicDBObject) items.get(minMemRatioIndex);
            }
        }
        return null;
    }

    /**
     * hashClientBySystemCapacity升级版本(4S==For Super)
     *
     * @param jobClass
     * @return
     */
    public Map hashClient4SBySystemCapacity(String jobClass) {
        Map map = new HashMap();
        BasicDBList items = getRecordsByJobClass(jobClass);
        int itemLen;
        if ((items == null) || ((itemLen = items.size()) == 0)) {
            map.put(CommonConstants.SUCCESS, false);
            map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_101);
            return map;
        } else if (1 == itemLen) {
            BasicDBObject item = (BasicDBObject) items.get(0);
            double memRatio = Double.valueOf(String.valueOf(item.get(CommonConstants.MEM_RATIO)));
            double cpuRatio = Double.valueOf(String.valueOf(item.get(CommonConstants.CPU_RATIO)));
            if (memRatio > CommonConstants.MAX_MEM_RATIO || cpuRatio > CommonConstants.MAX_CPU_RATIO) {
                logger.error("ClientNodes-->>hashClient4SBySystemCapacity(" + jobClass + ") the memRatio(" + memRatio + ")" +
                        " over max mem ratio(" + CommonConstants.MAX_MEM_RATIO + ") or the cpuRatio("+cpuRatio+")" +
                        " over max cpu ratio("+CommonConstants.MAX_CPU_RATIO+") ");
                map.put(CommonConstants.SUCCESS, false);
                map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_102);
                map.put(CommonConstants.MEM_RATIO, memRatio);
                map.put(CommonConstants.CPU_RATIO, cpuRatio);
                return map;
            } else {
                map.put(CommonConstants.SUCCESS, true);
                map.put(CommonConstants.CLIENT_JOB_INFO, item);
                return map;
            }
        } else if (itemLen > 1) {
            BasicDBObject tempItem = (BasicDBObject) items.get(0);
            int minMemRatioIndex = 0;
            double minMemRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.MEM_RATIO)));
            double minCpuRatioValue = Double.valueOf(String.valueOf(tempItem.get(CommonConstants.CPU_RATIO)));
            int minTotalThreadValue = Integer.valueOf(String.valueOf(tempItem.get(CommonConstants.TOTAL_THREAD)));
            double minTotalValue = minMemRatioValue + minCpuRatioValue + minTotalThreadValue;
            for (int i = 1; i < itemLen; i++) {
                tempItem = (BasicDBObject) items.get(i);
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
                logger.error("ClientNodes-->>hashClient4SBySystemCapacity(" + jobClass + ") " +
                        "the memRatio(" + minMemRatioValue + ") over max mem ratio(" + CommonConstants.MAX_MEM_RATIO + ")" +
                        "or the cpuRatio("+minCpuRatioValue+") over max cpu ration ("+CommonConstants.MAX_CPU_RATIO+") ");
                map.put(CommonConstants.SUCCESS, false);
                map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_102);
                map.put(CommonConstants.MEM_RATIO, minMemRatioValue);
                map.put(CommonConstants.CPU_RATIO, minCpuRatioValue);
                return map;
            } else {
                map.put(CommonConstants.SUCCESS, true);
                map.put(CommonConstants.CLIENT_JOB_INFO, items.get(minMemRatioIndex));
                return map;
            }
        }
        map.put(CommonConstants.SUCCESS, false);
        map.put(CommonConstants.ERROR_CODE, CommonConstants.ERROR_CODE_103);
        return map;
    }

    public boolean containsKey(String id) {
        return this.recordsById.containsKey(id);
    }

    public BasicDBList getRecordsByJobClass(String jobClass) {
        return this.recordsByJobClass.get(jobClass);
    }

    public BasicDBList findRecordsByIP(String ip) {
        BasicDBList basicDBList = new BasicDBList();
        for (int i = 0; i < this.records.size(); i++) {
            BasicDBObject oServer = (BasicDBObject) this.records.get(i);
            if (oServer.getString(CommonConstants.IP).equalsIgnoreCase(ip)) {
                basicDBList.add(oServer);
            }
        }
        return basicDBList;
    }

    public synchronized void add(BasicDBObject record) {
        String id = record.getString(CommonConstants.ID);
        int index = findIndexById(id, this.records);
        if (index == -1)
            this.records.add(record);
        else {
            this.records.set(index, record);
        }
        this.recordsById.put(id, record);
        if (!record.containsField(CommonConstants.JOB_CLASS)) {
            return;
        }
        BasicDBList jobClassList = (BasicDBList) record.get(CommonConstants.JOB_CLASS);
        for (int i = 0; (jobClassList != null) && (i < jobClassList.size()); i++) {
            String tempJobClass = jobClassList.get(i).toString();
            this.jobClassSet.add(tempJobClass);
            BasicDBList items = this.recordsByJobClass.get(tempJobClass);
            if (items == null) {
                items = new BasicDBList();
                this.recordsByJobClass.put(tempJobClass, items);
            }
            index = findIndexById(id, items);
            if (index == -1)
                items.add(record);
            else {
                items.set(index, record);
            }
        }
    }

    public synchronized boolean remove(String id) {
        this.recordsById.remove(id);
        int index = findIndexById(id, this.records);
        boolean flag = index != -1;
        if (flag) {
            this.records.remove(index);
        }
        for (Iterator i = this.recordsByJobClass.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            BasicDBList items = (BasicDBList) entry.getValue();
            index = findIndexById(id, items);
            if (index != -1) {
                items.remove(index);
            }
            if (items.size() == 0) {
                this.jobClassSet.remove(entry.getKey());
            }
        }
        return flag;
    }

    public void clear() {
        this.recordsById.clear();
        this.recordsByJobClass.clear();
        this.records.clear();
        this.jobClassSet.clear();
    }
}
