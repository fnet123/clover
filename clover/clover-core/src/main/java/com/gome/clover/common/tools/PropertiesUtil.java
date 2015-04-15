package com.gome.clover.common.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
 * Module Desc:Properties 工具类
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class PropertiesUtil {
    private static transient Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    public static Properties loadProperties(){
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream("zkConfig.properties");
            properties.load(in);
        } catch (Exception e) {
            if(logger.isDebugEnabled())e.printStackTrace();
            logger.error("loadProperties()方法异常",e);
        }finally {
            if(null != in ){
                try {
                    in.close();
                } catch (IOException e) {
                    if(logger.isDebugEnabled()) e.printStackTrace();
                    logger.error("close InputStream方法异常",e);
                }
            }
        }
        return  properties;
    }
    public static Properties  loadProperties(String propertiesPath){
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesPath);
            properties.load(in);
        } catch (Exception e) {
            if(logger.isDebugEnabled())e.printStackTrace();
            logger.error("loadProperties("+propertiesPath+")方法异常",e);
        }finally {
            if(null != in ){
                try {
                    in.close();
                } catch (IOException e) {
                    if(logger.isDebugEnabled()) e.printStackTrace();
                    logger.error("close InputStream方法异常",e);
                }
            }
        }
        return  properties;
    }

    public static boolean writePropertiesFile(String propertiesRealPath,Properties properties){
        OutputStream outputStream = null;
        try {
             outputStream = new FileOutputStream(propertiesRealPath);
             properties.store(outputStream,"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    public static void main(String args[]){
        String filePath = /*System.getProperty("user.dir") + File.separator+*/"zkConfig.properties";
        System.out.println(PropertiesUtil.loadProperties(filePath));
    }
}
