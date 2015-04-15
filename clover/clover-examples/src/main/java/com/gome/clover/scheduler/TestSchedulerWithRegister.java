package com.gome.clover.scheduler;

import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.core.module.ModuleSchedulerClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Module Desc:
 * User: wangyue-ds6
 * Date: 2014/11/11
 * Time: 22:54
 */
public class TestSchedulerWithRegister implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.err.println("TestScheduler-->>execute("+ context+")");
    }

    public static void main(String args[]){
        try {
            //启动Netty RPC
            //new RpcServer(CommonConstants.RPC_CLIENT_PORT).run();
            //ConsumerUtil.startup("10.58.50.204:9876;10.58.50.205:9876", CommonConstants.TOPIC_CLOVER_SERVER, IpUtil.getLocalIP().replace(".", "-"));
        } catch (Exception e) {

        }
        ModuleSchedulerClient client =  ModuleSchedulerClient.getInstance();
        client.registerToZK(CommonConstants.MODULE_TYPE_CLIENT,IpUtil.getLocalIP(),null,"-1",CommonConstants.SYSTEM_ID_CLOVER);

    }
}
