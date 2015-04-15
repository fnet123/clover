package com.gome.clover.common.hash;

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
 * Time: 14:00
 */
public class HashTimes
{
    public static int use33(String key)
    {
        int hashCode = 0;
        int i = 0;
        int len = key.length();
        for (; i < len; i++) {
            hashCode = hashCode * 33 + key.codePointAt(i) & 0x7FFFFFFF;
        }
        return hashCode;
    }

    public static int use37(String key) {
        return 0;
    }

    public static void main(String[] args)
    {
        for (int i = 0; i < 100; i++)
            System.out.println(i + "=" + use33(String.valueOf(i)) % 5);
    }
}