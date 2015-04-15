package com.gome.testclover.init;

import com.gome.clover.client.module.SchedulerClientInitial;
import com.gome.testclover.spring.MyRemoteJob;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

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
 * Module Desc:Init Servlet With Scheduler ClientInitial
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/17
 * Time: 11:42
 */
public class InitServletWithSchedulerClientInitial {

    public void init(){
        List jobClassList = new ArrayList();
        jobClassList.add(MyRemoteJob.class.getName());
        boolean isRegisterToZK = true;
        boolean isStartupMQ = true;
        String port = "-1";
        String systemId="test-clover";
        String token = "";
        try {
            new SchedulerClientInitial().init(jobClassList,isRegisterToZK,isStartupMQ,port,systemId,token);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
