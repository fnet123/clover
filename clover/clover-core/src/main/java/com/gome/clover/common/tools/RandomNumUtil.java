package com.gome.clover.common.tools;

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
 * Module Desc:Random NumUtil
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/4
 * Time: 21:48
 */
public class RandomNumUtil {
    private static final int DEFAULT_MIX_INT_VALUE = 9000;
    private static final int DEFAULT_MAX_INT_VALUE = 0xFFFF;

    public static int getNextInt() {
        synchronized (RandomNumUtil.class) {
            //ThreadLocalRandom.current().nextInt(0,65535);
            return new Random().nextInt(DEFAULT_MAX_INT_VALUE) % (DEFAULT_MAX_INT_VALUE - DEFAULT_MIX_INT_VALUE + 1) + DEFAULT_MIX_INT_VALUE;
        }
    }

    public static String getNextIntString() {
        synchronized (RandomNumUtil.class) {
            return String.valueOf(getNextInt());
        }
    }

    public static long getNextLong() {
        synchronized (RandomNumUtil.class) {
            return new Random(DEFAULT_MAX_INT_VALUE).nextLong() % (DEFAULT_MAX_INT_VALUE - DEFAULT_MIX_INT_VALUE + 1) + DEFAULT_MIX_INT_VALUE;
        }
    }
    public static String getNextLongString() {
        synchronized (RandomNumUtil.class) {
            return String.valueOf(getNextLong());
        }
    }

    public static int getNextInt(int minValue, int maxValue) {
        synchronized (RandomNumUtil.class) {
            return new Random().nextInt(maxValue) % (maxValue - minValue + 1) + minValue;
        }
    }

    public static String getNextIntString(int minValue, int maxValue) {
        synchronized (RandomNumUtil.class) {
            return String.valueOf(getNextInt(minValue, maxValue));
        }
    }

    public static long getNextLong(long minValue, long maxValue) {
        synchronized (RandomNumUtil.class) {
            return new Random(minValue).nextLong() % (maxValue - minValue + 1) + minValue;
        }
    }

    public static String getNextLongString(long minValue, long maxValue) {
        synchronized (RandomNumUtil.class) {
            return String.valueOf(getNextLong(minValue, maxValue));
        }
    }
}
