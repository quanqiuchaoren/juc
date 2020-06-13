package com.lhy.example.juc.creat_start;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建线程的第三种办法，运行这个程序，会看到主线程，由主线程创建的两个线程，这三个线程之间交替执行
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
        Thread callableDemo = new Thread(futureTask0, threadName0);
        Thread callableDem1 = new Thread(futureTask1, threadName1);
        for (int i = 0; i < 100; i++) {
            // 获取线程名字
            String mainThreadName = Thread.currentThread().getName();
            System.out.println(mainThreadName + "第" + i + "次执行");
            // 在执行到第5次循环的时候，启动线程
            if(5 == i){
                // 启动线程类，将线程类的实例加入可执行队列，供虚拟机调度
                callableDemo.start();
                callableDem1.start();
            }
        }
        // 获取线程执行体的返回值
        try {
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
     * @return
     * @throws Exception
     */
    @Override
    public Object call() throws Exception {
        int i = 0;
        for (; i < 100; i++) {
            // 获取当前的线程的名字
            String threadName = Thread.currentThread().getName();
            System.out.println("当前的线程的名字" + threadName + ",第" + i + "次执行");
        }
        return i;
    }
}