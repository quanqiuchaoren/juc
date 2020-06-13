package com.lhy.example.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by Enzo Cotter on 2020/6/7.
 */
public class SleepDemo2 {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new FutureTask<Object>(new MyCallable2()));
        thread1.start();
        try {
            /**
             * Thread.sleep()是属于Thread类的静态方法，在哪个线程调用，哪个线程睡眠
             * 即使是使用thread1这个线程实例来调用sleep()方法，
             * 编译之后，还是会编译成Thread.sleep()的调用形式
             */
            thread1.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + "第" + i + "次执行");
        }
    }

}

class MyCallable2 implements Callable{

    @Override
    public Object call()  {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + "第" + i + "次执行");
        }
        return null;
    }
}
