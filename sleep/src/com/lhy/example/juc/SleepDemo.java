package com.lhy.example.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by Enzo Cotter on 2020/6/7.
 */
public class SleepDemo {
    public static void main(String[] args) {
        new Thread(new FutureTask<Object>(new MyCallable())).start();
        try {
            /**
             * main方法里面，也是线程执行体，在main方法里面调用Thread.sleep()
             * 可以使主线程休眠
             */
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + "第" + i + "次执行");
        }
    }

}

class MyCallable implements Callable{

    @Override
    public Object call() throws Exception {
        /**
         * 在线程执行体里面使用Thread来调用sleep方法，单位为ms
         * 如果传入第二个参数，则单位为ns，总的时间就是X ms + Y ns
         * 在哪个线程执行体里面调用Thread.sleep()，就是哪个线程阻塞
         */
        Thread.sleep(3000);
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + "第" + i + "次执行");
        }
        return null;
    }
}
