package com.lhy.example.juc.join;

import java.util.concurrent.TimeUnit;

/**
 * join()方法的基本使用
 *      当前线程调用其他线程的join()之后，则当前线程会阻塞，知道被调用的线程执行完毕
 */
public class JoinDemo1 {
    public static void main(String[] args) throws InterruptedException {
        ThreadImpl thread1 = new ThreadImpl();
        thread1.start();
        /**
         * 在当前线程里面调用其他线程实例的join()方法，
         * 会使当前线程阻塞，如果没有传入超时时间，直到被调用join()方法的线程执行完毕
         */
        thread1.join();
        /**
         * main运行结束！这句话，会在thread1运行结束后才会被打印出来，因为join()方法使当前的主线程阻塞了
         */
        System.out.println(Thread.currentThread().getName() + "运行结束！");
    }
}


class ThreadImpl extends Thread{

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 100; i++) {
            System.out.println(this.getName() + "第" + i + "次执行");
        }
    }
}