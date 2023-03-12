package cn.lhy.example.juc.threadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用线程池的基本例子：ThreadPoolExecutor
 * ThreadPoolExecutor可以指定线程池的以下信息
 *      核心线程数：线程池中的线程的数量未达到核心线程数数量时，线程池接到一个任务，就会创建一个线程，并用创建的新的线程来执行任务
 *      最大线程数：当线程池中的线程数达到核心线程数时，且阻塞队列已满，如果有新的任务请求，则会创建新的线程来执行任务
 *      线程保活时间：当线程数超过核心线程数的时候，如果线程在超过保活时间之后，没有被使用，则会被线程池清理，实际上是线程池通知JVM清理该空闲线程
 *      指定容量的阻塞队列：当线程池中的线程数达到核心线程数时，新的任务请求会被存放到阻塞队列中，直到阻塞队列满为止
 *              使用指定容量的阻塞队列，不可扩容，则不会导致OOM（Out Of Memory：内存溢出）
 */
public class ThreadPoolExecutorDemo {
    public static void main(String[] args) {
        int corePoolSize = 5;
        int maximumPoolSize = 10;
        long keepAliveTime = 20;
        TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(10);
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, keepAliveTimeUnit, blockingQueue);
        for (int i = 0; i < 20; i++) {
            System.out.println("----------第" + i + "次提交请求");
            System.out.println("线程池中现有的线程数量：" + threadPoolExecutor.getPoolSize());
            System.out.println("队列中现有的请求数量：" + blockingQueue.size());
            System.out.println("已经完成的请求的数量：" + threadPoolExecutor.getCompletedTaskCount());
            Thread myTaskThread = new Thread(new MyTaskThread(), String.valueOf(i));
            threadPoolExecutor.execute(myTaskThread);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("主线程已经提交完所有的任务");
        for (int i = 0; i <100; i++) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("过了" + i*5 + "秒之后，当前线程池线程数量为" + threadPoolExecutor.getPoolSize());
            System.out.println("过了" + i*5 + "秒之后，队列中现有请求数量为" + blockingQueue.size());
        }
        // 本次模拟，只用了5个核心线程，最后线程数量达到了10个，就把所有的20个任务执行完毕，效率很高
        // 显示调用线程池的shutdown()方法，才可以使线程池死亡（terminate），程序才能停止
        threadPoolExecutor.shutdown();
    }
}

// 任务线程
class MyTaskThread implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "开始执行");
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "执行结束");
    }
}