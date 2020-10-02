package com.lhy.example.juc.join;

/**
 * join()方法的使用
 *      可传入一个超时时间
 */
public class JoinDemo4 {
    public static void main(String[] args) throws InterruptedException {
        ThreadImpl4 thread1 = new ThreadImpl4();

        thread1.start();
        /**
         * join(long millis)方法可以传入一个参数，是thread1的超时时间
         * 当超过这个事件，线程1还没有死亡，则main自动进入Runnable（可执行队列），供虚拟机调用
         * 如果传入的参数为0，则表示thread1不会超时，main会无限期等待thread1死亡
         */
        thread1.join(10);
        /**
         * main运行结束！这句话，会在thread1运行结束后才会被打印出来，因为join()方法使当前的主线程阻塞了
         */
        System.out.println(Thread.currentThread().getName() + "运行结束！");
    }
}


class ThreadImpl4 extends Thread{

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 1000; i++) {
            System.out.println(this.getName() + "第" + i + "次执行");
        }
    }
}