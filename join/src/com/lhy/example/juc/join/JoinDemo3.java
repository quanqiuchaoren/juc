package com.lhy.example.juc.join;

/**
 * join()的使用
 *      即使用多个线程，调用join()的线程，也只是在被调用join()的线程执行之后，就加入可执行队列了
 */
public class JoinDemo3 {
    public static void main(String[] args) throws InterruptedException {
        ThreadImpl3 thread1 = new ThreadImpl3();
        ThreadImpl3 thread2 = new ThreadImpl3();

        thread1.start();
        thread2.start();
        /**
         * 在有两个子线程的情况下
         * main线程对thread1使用了join()方法，此时，main线程阻塞，则main会在thread1运行结束后才运行
         * thread1和thread2会交替执行，
         * 但是只要thread1线程死亡，main线程就会重新进入Runnable队列中，供虚拟机调度
         */
        thread1.join();
        System.out.println(Thread.currentThread().getName() + "运行结束！");
    }
}


class ThreadImpl3 extends Thread{

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 1000; i++) {
            System.out.println(this.getName() + "第" + i + "次执行");
        }
    }
}