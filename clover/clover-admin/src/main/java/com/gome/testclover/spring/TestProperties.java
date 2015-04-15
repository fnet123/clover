package com.gome.testclover.spring;

import com.gome.clover.common.tools.PropertiesUtil;

import java.util.Properties;

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
 * Date: 2015/1/7
 * Time: 16:49
 */
public class TestProperties {
    public static void main(String[] args) {
        Properties properties = PropertiesUtil.loadProperties("conf/config.properties");
        String cloverServerStatus = (String) properties.get("cloverServerStatus");
        System.err.println(""+cloverServerStatus);
        properties.setProperty("cloverServerStatus","1");
        PropertiesUtil.writePropertiesFile("conf/config.properties",properties);
        properties = PropertiesUtil.loadProperties("conf/config.properties");
        System.err.println(""+properties.get("cloverServerStatus"));
    }
}
