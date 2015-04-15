package com.gome.testclover.init;

import com.gome.clover.core.module.ModuleSchedulerClient;
import com.gome.testclover.spring.MyRemoteJob;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
 * Module Desc:Init Spring Annotation With Startup
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/17
 * Time: 11:39
 */
@Component
public class InitSpringAnnotationWithStartup {

    @PostConstruct
    public void init() {
        System.err.println("InitSpringAnnotationWithStartup--->>>init().....");
        List jobClassList = new ArrayList();
        jobClassList.add(MyRemoteJob.class.getName());
        boolean isRegisterToZK = true;
        boolean isStartupMQ = true;
        String port = "-1";
        String systemId="test-clover";
        String token = "";
        ModuleSchedulerClient.getInstance().startup(jobClassList,isRegisterToZK,isStartupMQ,port,systemId,token);

    }

}
