package com.gome.clover;

import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.common.tools.RandomNumUtil;
import org.junit.Test;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
 * Date: 2014/12/3
 * Time: 14:57
 */
public class TestEum {
    @Test
    public void testEumVal() throws InterruptedException {
      /*  try{

        }catch (Exception e){
            System.err.println("testEumVal"+e.getMessage());
        }*/
        //System.err.println("" + DateUtil.formatWithDefaultPattern("2012-12-12 12:12:12"));
       /* while(true){
            int num = new Random().nextInt(0xFFFF);
            while (num == 2 || num == 7) {
                num = new Random().nextInt(100);
            }
            System.out.println(num);
        }*/
        while (true){
            Thread.sleep(1000);
            System.err.println(RandomNumUtil.getNextIntString());
        }

    }
    @Test
    public  void testLongMax(){
        Long maxLong = Long.MAX_VALUE;
        System.err.println(maxLong);
        int PID;
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String name = runtime.getName();
            PID = Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Throwable e) {
            PID = 0;
        }
        System.err.println("PID:"+PID);
    }

    @Test
    public void testCommon(){
        System.err.println(""+ CommonConstants.ZMQ_SERVER_PORT);
        System.err.println(""+ CommonConstants.SERVER_DIFFER_MILLI_SECONDS);
        System.err.println(""+ CommonConstants.CLIENT_DIFFER_MILLI_SECONDS);
        System.err.println(""+ CommonConstants.MONITOR_DIFFER_MILLI_SECONDS);
        System.err.println(""+ CommonConstants.DEFAULT_COMPANY_EMAIL);
        System.err.println(""+ CommonConstants.DEFAULT_PRIVATE_EMAIL);
        System.err.println(""+ CommonConstants.ZMQ_SLEEP_CLIENT_MILLIS);
        System.err.println(""+ CommonConstants.ZMQ_SLEEP_SERVER_MILLIS);
        System.err.println(""+ CommonConstants.token);
        System.err.println(""+ CommonConstants.DISABLED_DB);
        System.err.println(""+ CommonConstants.POOL_SIZE);
        System.err.println(""+ CommonConstants.MAX_FAIL_TIMES);

        int n = Runtime.getRuntime().availableProcessors()*2;
        System.err.println("n:"+n);

        //建立Socket
        Socket s = null;
        try {
             s = new Socket(IpUtil.getLocalIP(),27727);
            if(s.isConnected()){
                System.err.println("s.isConnected()");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=s && !s.isClosed()){
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testMap(){
        Map map  =  new HashMap();
        String acd = (String) map.get("adb");
        System.err.println(""+acd);
        map.put(CommonConstants.SUCCESS,false);

        boolean s = (Boolean) map.get(CommonConstants.SUCCESS);
        System.err.println(""+s );
    }
}
