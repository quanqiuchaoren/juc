package com.lhy.example.juc.creat_start;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 创建线程的第三种办法：使用Callable接口，这种办法，线程可以有返回值
 *      当然，如果其他线程想要获取返回值，当还没返回的时候，会让其他获取返回值的线程阻塞
 * 运行这个程序，会看到主线程，由主线程创建的两个线程，这三个线程之间交替执行
 *
 */
public class CallableDemo {
    public static void main(String[] args) {
        // 创建我们写的可调用类实例，可调用类实例可以被多个任务类共享
        CallableImpl callableImpl = new CallableImpl();
        // 将可调用类作为构造参数，创建一个线程任务类实例
        FutureTask<Integer> futureTask0 = new FutureTask<>(callableImpl);
        FutureTask<Integer> futureTask1 = new FutureTask<>(callableImpl);
        String threadName0 = "thread-0";
        String threadName1 = "thread-1";
        // 将线程任务类实例作为构造参数，创建一个线程类实例，第二个参数为线程的名字
        Thread thread1 = new Thread(futureTask0, threadName0);
        Thread thread2 = new Thread(futureTask1, threadName1);
        // 启动线程类，将线程类的实例加入可执行队列，供虚拟机调度
        thread1.start();
        thread2.start();
        // 获取线程执行体的返回值
        try {
            // 子线程沉睡2秒，这里获取结果的时候
            Integer threadExecuteBodyResult0 = futureTask0.get();
            Integer threadExecuteBodyResult1 = futureTask1.get();
            System.out.println(threadName0 + "的线程执行体的返回值为->" + threadExecuteBodyResult0);
            System.out.println(threadName1 + "的线程执行体的返回值为->" + threadExecuteBodyResult1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("-------程序结束");
    }
}

/**
 * 创建一个可调用类，继承可调用接口，即java.util.concurrent.Callable
 * Callable是从jdk1.5开始有的
 */
class CallableImpl implements Callable{
    /**
     * 重写call方法，这个call方法，类似于继承Runnable接口的时候的run方法，也是一个执行体
     */
    @Override
    public Object call() {
        try {
            // 让线程沉睡2秒
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Random().nextInt();
    }
}