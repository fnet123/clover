package com.gome.clover.listener;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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
 * Module Desc:Log4j ConfigListener
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/25
 * Time: 10:55
 */
public class Log4jConfigListener implements ServletContextListener{

    public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";
    public static final String XML_FILE_EXTENSION = ".xml";

    public void contextDestroyed(ServletContextEvent event) {
        LogManager.shutdown();
    }

    public void contextInitialized(ServletContextEvent event) {
        String location = event.getServletContext().getInitParameter(CONFIG_LOCATION_PARAM);
        if (location != null) {
            if (!location.startsWith("/")) {
                location = "/" + location;
            }
            location = event.getServletContext().getRealPath(location);

            //如果是xml结尾就用DOM解析，否则就用properties解析
            if (location.toLowerCase().endsWith(XML_FILE_EXTENSION)) {
                DOMConfigurator.configure(location);
            }else {
                PropertyConfigurator.configure(location);
            }
        }
    }
}
