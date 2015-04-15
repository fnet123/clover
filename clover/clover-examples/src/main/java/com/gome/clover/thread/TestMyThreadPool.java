package com.gome.clover.thread;

import com.gome.clover.common.tools.CommonConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
 * Date: 2014/12/18
 * Time: 15:21
 */
public class TestMyThreadPool {
    private static ExecutorService pool = null; //线程池
    public static void main(String[] args) {
        // 创建3个线程的线程池
      /*  MyThreadPool t = MyThreadPool.getThreadPool();
        t.execute(new Runnable[] { new Task(), new Task(), new Task() });
        t.execute(new Runnable[] { new Task(), new Task(), new Task() });
        System.out.println(t);
        t.destroy();// 所有线程都执行完成才destory
        System.out.println(t);*/
        test1();
        test2();
        test3();
    }

    public static void test1(){
        if (null == pool) {
            pool = Executors.newFixedThreadPool(CommonConstants.POOL_SIZE);
            System.err.println("test1->Executors.newFixedThreadPool(CommonConstants.POOL_SIZE)");
        }
        pool.execute(new Thread(new Task()));
    }

    public static void test2(){
        if (null == pool) {
            pool = Executors.newFixedThreadPool(CommonConstants.POOL_SIZE);
            System.err.println("test2->Executors.newFixedThreadPool(CommonConstants.POOL_SIZE)");
        }
        pool.execute(new Thread(new Task()));
    }
    public static void test3(){
        if (null == pool) {
            pool = Executors.newFixedThreadPool(CommonConstants.POOL_SIZE);
            System.err.println("test3->Executors.newFixedThreadPool(CommonConstants.POOL_SIZE)");
        }
        pool.execute(new Thread(new Task()));
    }

    // 任务类
    static class Task implements Runnable {
        private static volatile int i = 1;

        @Override
        public void run() {// 执行任务
            System.out.println("任务 " + (i++) + " 完成");
        }
    }
}