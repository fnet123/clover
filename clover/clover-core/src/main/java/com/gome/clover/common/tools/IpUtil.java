package com.gome.clover.common.tools;

import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
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
 * Module Desc:IpUtil
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class IpUtil {

	public static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

	public static String getLocalIP() {
		Enumeration<NetworkInterface> netInterfaces = null;
		String localIp = "127.0.0.1";
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (Exception e) {
            logger.error(e.getMessage(), e);
		}
		if (netInterfaces == null)
			return localIp;
		ArrayList<BasicDBObject> items = new ArrayList<BasicDBObject>();
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = netInterfaces.nextElement();
			String name = ni.getName().toLowerCase();
			if (name.startsWith("lo") || name.startsWith("vir") || name.startsWith("vmnet") || name.startsWith("wlan"))
				continue;
			Enumeration<InetAddress> ips = ni.getInetAddresses();
			while (ips.hasMoreElements()) {
				InetAddress ia = ips.nextElement();
				if ((ia instanceof Inet4Address) == false)
					continue;
				if (ia.getHostAddress().toString().startsWith("127"))
					continue;
				items.add(new BasicDBObject().append("name", name).append("ip", ia.getHostAddress()));
			}
		}
		if (items.size() == 0)
			return localIp;
		Collections.sort(items, new Comparator<BasicDBObject>() {
			@Override
			public int compare(BasicDBObject o1, BasicDBObject o2) {
				return o1.getString("name").compareToIgnoreCase(o2.getString("name"));
			}
		});
		return items.get(0).getString("ip");
	}

	public static String getLocalName() {
		BufferedReader reader = null;
		try {
			Runtime run = Runtime.getRuntime();
			Process proc = run.exec("hostname");
			StringWriter result = new StringWriter();
			reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null)
				result.append(line);
			return result.toString();
		} catch (Exception e) {
			return "unknow local name";
		} finally {
			closeReader(reader);
		}
	}

	private static void closeReader(Reader reader) {
		if (reader == null)
			return;
		try {
			reader.close();
		} catch (IOException e) {
            logger.error(e.getMessage(), e);
		}
	}
}