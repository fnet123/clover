package com.gome.clover.common.tools;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
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
 * Module Desc:ProgressUtil
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class ProgressUtil {
	public static void main(String[] args) throws InterruptedException {
		System.out.println(getRunTimeInfo());
		System.out.println(getPid());
		Thread.sleep(1000000000000000L);
	}

	public static int getPid() {
		String info = getRunTimeInfo();
		int pid = new Random().nextInt();
		int index = info.indexOf("@");
		if (index > 0) {
			pid = Integer.parseInt(info.substring(0, index));
		}
		return pid;
	}

	public static String getRunTimeInfo() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String info = runtime.getName();
		return info;
	}
}
