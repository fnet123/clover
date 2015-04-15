package com.gome.clover.time;

import com.gome.clover.common.tools.CommonConstants;
import org.junit.Test;

/**
 * Module Desc:
 * User: wangyue-ds6
 * Date: 2014/11/21
 * Time: 12:23
 */
public class TestTime {
    @Test
    public  void  getCurrentTime(){
        Long currentTimeMillis = System.currentTimeMillis(); //系统时间
        System.out.println("currentTimeMillis:"+currentTimeMillis);

        Long lastTimeMillis = 1416544031687L;
        System.err.println("DIFF:"+(currentTimeMillis-lastTimeMillis)/(1000*60));
        if(currentTimeMillis-lastTimeMillis<= CommonConstants.SERVER_DIFFER_MILLI_SECONDS){
            System.err.println("SERVER_DIFFER_MILLI_SECONDS");
        }


    }
}
