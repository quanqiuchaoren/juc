package com.lhy.example.juc.join;

/**
 * Created by Enzo Cotter on 2020/6/6.
 */
public class JoinDemo2 {
    public static void main(String[] args) throws InterruptedException {
        ThreadImpl2 thread1 = new ThreadImpl2();

        thread1.start();
        /**
         * 在当前线程里面调用其他线程实例的join()方法，
         * 会使当前线程阻塞，如果没有传入超时时间，直到被调用join()方法的线程执行完毕
         */
//        thread1.join();
        /**
         * 上面的join方法被注释掉后，“main运行结束！”可能就会在thread1分配到时间片之前，就执行了
         * 因为如果没有调用join()方法，主线程和thread1线程还是并发执行的，还是会抢cpu的时间片
         */
        System.out.println(Thread.currentThread().getName() + "运行结束！");
    }
}


class ThreadImpl2 extends Thread{

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 100; i++) {
            System.out.println(this.getName() + "第" + i + "次执行");
        }
    }
}