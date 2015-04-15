package com.gome.clover.common.tools;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
 * Module Desc:字符串与List 转换工具
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class List2StringUtil {
    /**
     * json串转List集合
     * @param jsonStr
     * @return List 集合
     */
    public static List json2List(String jsonStr){
        return JSON.parseObject(jsonStr,List.class);
    }

    /**
     * List集合转json串
     * @param list 集合
     * @return
     */
    public static String list2JsonStr(List list){
        return JSON.toJSONString(list);
    }

    /**
     * 从List集合中随机获取一个值
     * @param list 集合
     * @return
     */
    public static  String  getRandomValueFromList(List<String> list){
        if(0==list.size())return null;
        int len = list.size();
        Random random = new Random();
        return list.get(random.nextInt(len));
    }

    /**
     * 添加一个List集合中不存在的值
     * @param str
     * @param list
     * @return true:添加成功;false:添加失败
     */
    public static boolean addUnDuplicateString2List(String str, List<String> list){
        if(!list.contains(str)){
            list.add(str);
            return true;
        }
        return false;
    }
    /**
     * 添加一个List集合中不存在的值
     * @param str
     * @param list
     * @return true:添加成功;false:添加失败
     */
    public static boolean addUnDuplicateString2List2(String str, List<String> list){
        if(!list.contains(str)){
            list.add(str);
            return true;
        }
        return false;
    }

    public static  String[]  listToArray(List<String> list){
        int len = list.size();
        String[] strs = new String[len];
        int index = 0;
        for(String str : list){
            strs[index++] = str;
        }
        return strs;
    }
    public static void remove(List<String> list,String str){
        if(list.contains(str)){
            list.remove(str);
        }
    }
    public static List<String> getServerIpList(List<String> serverDataList){
        List<String> ipList  = new ArrayList<String>();
        for(String serverDataStr : serverDataList){//127.0.0.1_#_1_#_124388347830(ip_#_alive_#_ts)
            String [] tempSplitServerDatas =  serverDataStr.split(CommonConstants.SPLIT_CHARACTER_FALG);
            if(null != tempSplitServerDatas &&CommonConstants.ALIVE_STATUS_1.equals(tempSplitServerDatas[1])){
                ipList.add(tempSplitServerDatas[0]);
            }
        }
        return ipList;
    }
    public static byte[] toBytes(String content) {
        try {
            return content.getBytes("utf-8"); } catch (Exception e) {
        }
        return content.getBytes();
    }

    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, "utf-8"); } catch (Exception e) {
        }
        return new String(bytes);
    }
}
