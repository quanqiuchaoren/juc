package cn.lhy.example.juc.threadPool;

import java.util.concurrent.*;

/**
 * 基本代码
 */
public class HelloThreadPool {
    public static void main(String[] args) {
        int corePoolSize = 5;
        int maximumPoolSize = 10;
        long keepAliveTime = 120;
        TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
        // 线程安全的阻塞队列
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(100);
        // 创建任务
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, // 核心线程数
                maximumPoolSize, // 最大线程数
                keepAliveTime, keepAliveTimeUnit, // 线程保活时间
                blockingQueue, // 阻塞队列
                Executors.defaultThreadFactory(), // 线程工厂
                new ThreadPoolExecutor.AbortPolicy() // AbortPolicy是默认的拒绝策略：拒绝当前请求，且抛出异常
        );
        // 向线程池提交一个任务
        threadPoolExecutor.execute(() -> System.out.println("运行提交的任务结束"));
        threadPoolExecutor.shutdown();
    }
}

