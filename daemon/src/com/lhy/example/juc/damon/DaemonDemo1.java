package com.lhy.example.juc.damon;

/**
 * 守护线程演示
 */
public class DaemonDemo1 {
    public static void main(String[] args) {
        DaemonThread daemonThread = new DaemonThread("守护线程");
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
        // 创建一个前台线程，当前台线程（包括主线程和这个子线程）都结束的时候，后台线程就随之死亡
        new Thread(new FrontThread(), "前台线程").start();
        System.out.println("主线程功能性代码执行完毕");
    }
}

class DaemonThread extends Thread{
    public DaemonThread(String threadName){
        super(threadName);
    }
    @Override
    public void run() {
        super.run();
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

class FrontThread implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "第" + i + "次循环");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

