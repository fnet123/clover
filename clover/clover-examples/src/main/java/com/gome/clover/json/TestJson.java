package com.gome.clover.json;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Module Desc:
 * User: wangyue-ds6
 * Date: 2014/11/17
 * Time: 20:08
 */
public class TestJson {
    @Test
    public void setToJson(){
        System.err.println("setToJson");
        Set<String> set  = new HashSet<String>();
        set.add("127.0.0.1");
        set.add("192.168.168.1");
        String setJson = JSON.toJSONString(set);
        System.err.println("setJson "  +setJson);

        Set<String> set1 = JSON.parseObject(setJson,Set.class);
        System.err.println("set "  +set1);
    }
    @Test
    public void jsonToSet(){
       String setJson = "[\"192.168.168.1\",\"127.0.0.1\"]";
        Set<String> set = JSON.parseObject(setJson, Set.class);
        System.err.println("set "  +set);
        int len = set.size();
        Math.random();
        Random random = new Random();
        int newVal = random.nextInt(len);
        System.err.println("newVal:"+newVal);
    }

    @Test
    public void listToJson(){
        List<String> ipList = new CopyOnWriteArrayList<String>();
        if(!ipList.contains("127.0.0.1")){
            ipList.add("127.0.0.1");
        }
        if(!ipList.contains("192.168.168.1")){
            ipList.add("192.168.168.1");
        }
        String listJson = JSON.toJSONString(ipList);
        System.err.println("listJson "  +listJson);

        List<String> ipList2 = JSON.parseObject(listJson,List.class);
        System.err.println("list "  +ipList2);

        int len = ipList2.size();
        Math.random();
        Random random = new Random();
        int newVal = random.nextInt(len);
        System.err.println("newVal:"+newVal);
    }

    public void testList2StringUtil(){

    }

    @Test
    public void testMap(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("alive","1");
        map.put("ip","127.0.0.1");
        String mapStr = JSON.toJSONString(map);
        System.err.println("mapStr-----"+mapStr);
        Map<String,String> map1 = JSON.parseObject(mapStr,Map.class);
        System.err.println("map1-----------"+map1);
    }
    @Test
    public void testArray(){
        String a[] = new String[]{};
        List<String> list = new ArrayList<String>();
        list.add("2");
        System.err.println("a"+JSON.toJSONString(a));
        System.err.println("list"+list);
    }

}
