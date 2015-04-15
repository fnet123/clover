package com.gome.clover.monitor;

import com.gome.clover.common.tools.CommonConstants;
import com.mongodb.BasicDBList;

import java.util.Timer;

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
 * Module Desc: Server Heart Beat
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */
public enum ServerHeartBeatBak {
    INSTNACE, ServerHeartBeat;
   public void startup(){
       Timer timer = new Timer();
       BasicDBList recordList = new BasicDBList();
       timer.schedule(new ServerTimerTask(recordList),CommonConstants.SERVER_DIFFER_MILLI_SECONDS, CommonConstants.SERVER_DIFFER_MILLI_SECONDS);
   }
    public static void main(String args[]){
        ServerHeartBeat.INSTNACE.startup();
    }
}
