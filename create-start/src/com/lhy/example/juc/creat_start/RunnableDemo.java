package com.lhy.example.juc.creat_start;

/**
 * Created by Enzo Cotter on 2020/6/6.
 */
public class RunnableDemo {
    public static void main(String[] args) {
        Runnable runnable = new RunnableImpl();
        // 多个线程，可以共享一个Runnable的实现类
        Thread thread1 = new Thread(runnable);
        // 多个线程，可以共享一个Runnable的实现类
        Thread thread2 = new Thread(runnable);
        // 获取线程的名字
        String threadName = Thread.currentThread().getName();
        for (int i = 0; i < 100; i++) {
            System.out.println(threadName + "第" + i + "次执行");
            if(5 == i){
                thread1.start();
                thread2.start();
            }
        }
    }
}


/**
 * Runnable的实现类的实例，可以作为Thread类的构造函数的参数，然后被JVM调度
 */
class RunnableImpl implements Runnable{
    /**
     * Runnable的实现类没有返回值，并且不会抛出检查异常
     */
    @Override
    public void run() {
        // 获取线程的名字
        String threadName = Thread.currentThread().getName();
        for (int i = 0; i < 100; i++) {
            System.out.println(threadName + "第" + i + "次执行");
        }
    }
}