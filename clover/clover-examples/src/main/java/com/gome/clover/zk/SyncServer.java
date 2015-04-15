package com.gome.clover.zk;

import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.zk.ZKConnect;
import org.junit.Test;

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
 * Module Desc:Sync Server
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/18
 * Time: 22:25
 */
public class SyncServer {

    public static void main(String[] args) {
        MonitorServerZKListener monitorServerZKListener =  new MonitorServerZKListener(CommonConstants.ZK_ROOT_PATH + "/server");
       // zkConnect.addListener(monitorServerZKListener);
        //monitorServerZKListener.reload();
    }
    @Test
    public void test1(){
        ZKConnect  zkConnect = new ZKConnect();
        if ((zkConnect == null) || (!zkConnect.start())) {

        }
        //MonitorServerZKListener monitorServerZKListener =  new MonitorServerZKListener(CommonConstants.ZK_ROOT_PATH + "/server",zkConnect);
        //zkConnect.addListener(monitorServerZKListener);
       // monitorServerZKListener.reload();
    }

}
