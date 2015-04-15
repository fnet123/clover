package com.gome.clover.common.file;

import com.gome.clover.common.tools.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.ResourceBundle;

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
 * Time: 15:09
 */

public class ConfigFile {
    protected static Logger logger = LoggerFactory.getLogger(ConfigFile.class);
    private ResourceBundle rb = null;
    private static ConfigFile commonConfig = null;
    private static ConfigFile mongoDBConfig = null;
    private static ConfigFile zkConfig = null;
    private static ConfigFile redisConfig = null;

    static {
        logger.info("load mongoDBConfig properities & zkConfig & url & redisConfig properities.");
        commonConfig = new ConfigFile(ResourceBundle.getBundle("commonConfig"));
        mongoDBConfig = new ConfigFile(ResourceBundle.getBundle("mongoDBConfig"));
        zkConfig = new ConfigFile(ResourceBundle.getBundle("zkConfig"));
        redisConfig = new ConfigFile(ResourceBundle.getBundle("redisConfig"));
    }

    public ConfigFile(ResourceBundle rb) {
        this.rb = rb;
    }

    public static ConfigFile commonConfig() {
        return commonConfig;
    }
    public static ConfigFile mongoDBConfig() {
        return mongoDBConfig;
    }
    public static ConfigFile zkConfig() {return zkConfig;}
    public static ConfigFile redisConfig() {return redisConfig;}

    public String getItem(String item,String defaultValue) {
        String value = null;
        if (this.rb != null) {
            try {
                value = this.rb.getString(item.trim());
                value = value.trim();
            } catch (Exception e) {
                value = defaultValue;
            }
        }
        if (StringUtil.isEmpty(value)) {
            value = defaultValue;
        }
        return value;
    }

    public int getIntItem(String item, String defaultValue) {
        int i = 0;
        String value = getItem(item,defaultValue);
        try {
            i = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.info(e.getMessage());
        }
        return i;
    }
    public long getLongItem(String item, String defaultValue) {
        long i = 0;
        String value = getItem(item,defaultValue);
        try {
            i = Long.valueOf(value);
        } catch (NumberFormatException e) {
            logger.info(e.getMessage());
        }
        return i;
    }
    public double getDoubleItem(String item, String defaultValue) {
        double i = 0;
        String value = getItem(item,defaultValue);
        try {
            i = Double.valueOf(value);
        } catch (NumberFormatException e) {
            logger.info(e.getMessage());
        }
        return i;
    }

    public boolean getBooleanItem(String item, boolean defaultValue) {
        boolean b = false;
        String value = getItem(item, new Boolean(defaultValue).toString());
        if ((value != null) && (value.equalsIgnoreCase("true"))) {
            b = true;
        }
        return b;
    }

    protected String getNodeValue(Node _node) {
        if (_node == null) {
            return null;
        }
        Node _firstChild = _node.getFirstChild();
        if (_firstChild == null) {
            return null;
        }
        String _text = _firstChild.getNodeValue();
        if (_text != null) {
            _text = _text.trim();
        }
        return _text;
    }
}