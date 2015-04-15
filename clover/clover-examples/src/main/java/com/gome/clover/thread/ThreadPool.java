package com.gome.clover.thread;/**
 * Created by  on .
 */

import java.util.LinkedList;

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
 * Time: 22:54
 */
public class ThreadPool extends ThreadGroup {
    private boolean isClosed = false;  //线程池是否关闭
    private LinkedList workQueue;      //工作队列
    private static int threadPoolID = 1;  //线程池的id
    public ThreadPool(int poolSize) {  //poolSize 表示线程池中的工作线程的数量
        super(threadPoolID + "");      //指定ThreadGroup的名称
        setDaemon(true);               //继承到的方法，设置是否守护线程池
        workQueue = new LinkedList();  //创建工作队列
        for(int i = 0; i < poolSize; i++) {
            new WorkThread(i).start();   //创建并启动工作线程,线程池数量是多少就创建多少个工作线程
        }
    }

    /** 向工作队列中加入一个新任务,由工作线程去执行该任务*/
    public synchronized void execute(Runnable task) {
        if(isClosed) {
            throw new IllegalStateException();
        }
        if(task != null) {
            workQueue.add(task);//向队列中加入一个任务
            notify();           //唤醒一个正在getTask()方法中待任务的工作线程
        }
    }

    /** 从工作队列中取出一个任务,工作线程会调用此方法*/
    private synchronized Runnable getTask(int threadId) throws InterruptedException {
        while(workQueue.size() == 0) {
            if(isClosed) return null;
            System.out.println("工作线程"+threadId+"等待任务...");
            wait();             //如果工作队列中没有任务,就等待任务
        }
        System.out.println("工作线程"+threadId+"开始执行任务...");
        return (Runnable) workQueue.removeFirst(); //反回队列中第一个元素,并从队列中删除
    }

    /** 关闭线程池 */
    public synchronized void closePool() {
        if(! isClosed) {
            waitFinish();        //等待工作线程执行完毕
            isClosed = true;
            workQueue.clear();  //清空工作队列
            interrupt();        //中断线程池中的所有的工作线程,此方法继承自ThreadGroup类
        }
    }

    /** 等待工作线程把所有任务执行完毕*/
    public void waitFinish() {
        synchronized (this) {
            isClosed = true;
            notifyAll();            //唤醒所有还在getTask()方法中等待任务的工作线程
        }
        Thread[] threads = new Thread[activeCount()]; //activeCount() 返回该线程组中活动线程的估计值。
        int count = enumerate(threads); //enumerate()方法继承自ThreadGroup类，根据活动线程的估计值获得线程组中当前所有活动的工作线程
        for(int i =0; i < count; i++) { //等待所有工作线程结束
            try {
                threads[i].join();  //等待工作线程结束
            }catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 内部类,工作线程,负责从工作队列中取出任务,并执行
     * @author sunnylocus
     */
    private class WorkThread extends Thread {
        private int id;
        public WorkThread(int id) {
            //父类构造方法,将线程加入到当前ThreadPool线程组中
            super(ThreadPool.this,id+"");
            this.id =id;
        }
        public void run() {
            while(! isInterrupted()) {  //isInterrupted()方法继承自Thread类，判断线程是否被中断
                Runnable task = null;
                try {
                    task = getTask(id);     //取出任务
                }catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
                //如果getTask()返回null或者线程执行getTask()时被中断，则结束此线程
                if(task == null) return;

                try {
                    task.run();  //运行任务
                }catch(Throwable t) {
                    t.printStackTrace();
                }
            }//  end while
        }//  end run
    }// end workThread
}
