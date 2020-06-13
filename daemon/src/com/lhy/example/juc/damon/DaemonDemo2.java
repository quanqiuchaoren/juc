package com.lhy.example.juc.damon;

/**
 * 守护线程演示
 *
 * 前台线程创建的线程默认为前台线程，后台线程创建的线程默认为后台线程
 */
public class DaemonDemo2 {
    public static void main(String[] args) {
        DaemonThread2 daemonThread = new DaemonThread2("守护线程");
        /**
         * setDaemon(boolean on)：是否开启守护线程开关
         * 当传入参数为true时，则表示这是一个守护线程
         * 守护线程不能单独在进程里面存在，当所有的前台线程都死亡时，守护线程也随之死亡，进程结束
         * 守护进程死亡，前台线程还是可以继续执行
         */
        daemonThread.setDaemon(true);
        daemonThread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class DaemonThread2 extends Thread{
    public DaemonThread2(String threadName){
        super(threadName);
    }
    @Override
    public void run() {
        /**
         * 在守护线程里面创建一个线程，默认是守护线程
         * 需要显式指定为前台线程，才能成为前台线程
         */
        Thread frontThread = new Thread(new FrontThread2(), "前台线程");
        // 显式指定在守护线程里面创建的线程为前台线程
        frontThread.setDaemon(false);
        frontThread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println(this.getName()+"第" + i + "次循环");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class FrontThread2 implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "第" + i + "次循环");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

