package com.gome.clover.common.compress;

import org.xerial.snappy.Snappy;

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
 * Module Desc:Compress Util
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/4
 * Time: 22:36
 */
public class CompressUtil {
    public static byte[] compress(byte[] orig) {
        try {
            return Snappy.compress(orig);
        } catch (Exception e) {
        }
        return orig;
    }

    public static byte[] uncompress(byte[] compressed) {
        try {
            return Snappy.uncompress(compressed);
        } catch (Exception e) {
        }
        return compressed;
    }
}
