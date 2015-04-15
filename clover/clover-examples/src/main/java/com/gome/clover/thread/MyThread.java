package com.gome.clover.thread;/**
 * Created by  on .
 */

import com.gome.clover.common.mongodb.MongoDBUtil;
import com.gome.clover.common.tools.DateUtil;
import com.gome.clover.common.tools.IpUtil;
import com.mongodb.BasicDBObject;

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
 * Date: 2014/11/25
 * Time: 22:56
 */
class MyThread implements java.lang.Runnable
{
    private int threadId;
    public MyThread(int id)
    {
        this.threadId = id;
    }
    @Override
    public  void run()
    {
        BasicDBObject document = new BasicDBObject();
        document.put("jobKey", "jobKey1");
        document.put("jobInfo", "jobInfo1");
        document.put("ip", IpUtil.getLocalIP());
        document.put("ts", DateUtil.currentDateTime());
        MongoDBUtil.INSTANCE.insert(document,"test_wy");
        MongoDBUtil.INSTANCE.insertOrUpdate(document,"test_wy");
        System.err.println("MyThread--->>>run()");
    }
}