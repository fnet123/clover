package com.gome.clover.common.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigUtil {

	private Properties prop;
	private final String propFile = "Config.properties";
	private volatile long lastModifiedTime;// 最后修改时间
	private File xmlFile;
	private Lock lock = null;
	private static Log log = LogFactory.getLog(ConfigUtil.class);

	private static final ConcurrentHashMap<String, ConfigUtil> proMap = new ConcurrentHashMap<String, ConfigUtil>();

	private ConfigUtil(String propFile) {
		if (lock == null) {
			lock = new java.util.concurrent.locks.ReentrantLock();
		}
		xmlFile = new File(Thread.currentThread().getContextClassLoader().getResource(propFile).getFile());
		if (xmlFile.exists()) {
			lastModifiedTime = xmlFile.lastModified();
		} else {
			log.info("配置文件" + propFile + "不存在，请检查。");
		}
		this.initPropertFile();

	}

	public static ConfigUtil getInstance(String propFile) {
		if (proMap.get(propFile) == null) {
			proMap.putIfAbsent(propFile, new ConfigUtil(propFile));
		}
		return proMap.get(propFile);
	}

	/**
	 * 初始化配置文件
	 */
	public void initPropertFile() {
		if (lock == null) {
			lock = new java.util.concurrent.locks.ReentrantLock();
		}
		if (prop == null || this.checkFileReload()) {
			lock.lock();
			prop = new Properties();
			try {
				InputStream ins = new FileInputStream(xmlFile);
				prop.load(ins);
			} catch (Exception e) {
				log.error("加载Config.properties 文件出错:" + e.getMessage());
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * 获取值
	 * 
	 * @return
	 */
	public String getValue(String key) {
		this.initPropertFile();
		String v = prop.getProperty(key);
		if (StringUtils.isBlank(v)) {
			return "";
		}
		return v.trim();
	}

	public synchronized void setValue(String key, String value) {
		this.initPropertFile();
		prop.setProperty(key, value);
	}

	/**
	 * 判断是否需要重新加载xml文件
	 */
	private boolean checkFileReload() {
		long new_time = xmlFile.lastModified();
		boolean returnresult = false;// 是否需要重新加载，false不需要，true需要
		if (new_time == 0) {
			log.info("配置文件" + propFile + "不存在，请检查。");
		} else if (new_time > lastModifiedTime) {
			lastModifiedTime = new_time;
			returnresult = true;
		}
		return returnresult;
	}

}
