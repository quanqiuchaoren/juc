package cn.lhy.example.juc.threadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 实验：当线程池中的线程数小于核心线程数的数量时，且有空闲线程，
 * 当有新的执行请求到达时，是新建线程，还是使用空闲的线程
 * 结论：使用空闲线程来执行新的请求
 * <p>
 * 下列实验执行结果
 * 第 - 1 -次提交的任务开始执行
 * 第 - 1 -次提交的任务执行完毕
 * 第一个请求执行完毕之后，线程池有1个线程
 * 第 - 2 - 次提交的任务开始执行
 * 第 - 2 - 次提交的任务执行完毕
 * 第二个请求执行完毕之后，线程池有1个线程
 */
public class ThreadPoolExecutorDemo_corePoolSize {
    public static void main(String[] args) {
        int corePoolSize = 2;
        int maximumPoolSize = 2;
        long keepAliveTime = 60;
        TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, keepAliveTimeUnit, blockingQueue);
        threadPoolExecutor.execute(() -> System.out.println("第 - 1 -次提交的任务执行完毕"));
        try {
            /*
             * 用主线程休眠2秒来保证第二个请求提交时，第一个请求已经执行完毕，
             * 创建第一个线程处于空闲状态
             */
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("第一个请求执行完毕之后，线程池有" + threadPoolExecutor.getPoolSize() + "个线程");
        // 第一个请求执行完毕之后，线程池有1个线程

        threadPoolExecutor.execute(() -> System.out.println("第 - 2 - 次提交的任务执行完毕"));
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
         * 第二次提交任务后，线程池有两个线程，说明又新建了一个线程。表明当线程池的线程数小于核心线程数时，又来一个新的任务，也会先创建新的线程
         */
        System.out.println("第二个请求执行完毕之后，线程池有" + threadPoolExecutor.getPoolSize() + "个线程");
        // 第二个请求执行完毕之后，线程池有2个线程

        threadPoolExecutor.shutdown();
    }
}

