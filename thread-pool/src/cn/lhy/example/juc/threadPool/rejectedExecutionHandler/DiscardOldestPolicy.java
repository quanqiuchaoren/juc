package cn.lhy.example.juc.threadPool.rejectedExecutionHandler;

import java.util.concurrent.*;

/**
 * 实验，达到核心线程数之后，阻塞队列也已经满，有新的请求时，如何处理
 * DiscardOldestPolicy：即丢弃掉队列中最老的请求，且不会抛出异常
 */
public class DiscardOldestPolicy {
    public static void main(String[] args) {
        int corePoolSize = 1;
        int maximumPoolSize = 1;
        long keepAliveTime = 60;
        TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(2);
        // 采用DiscardOldestPolicy，即丢弃掉队列中最老的请求，且不会抛出异常
        ThreadPoolExecutor.DiscardOldestPolicy rejectPolicy = new ThreadPoolExecutor.DiscardOldestPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, // 核心线程数
                maximumPoolSize, // 最大线程数
                keepAliveTime, keepAliveTimeUnit, // 线程保活时间
                blockingQueue, // 阻塞队列
                Executors.defaultThreadFactory(), // 线程工厂
                rejectPolicy // 拒绝策略，默认的拒绝策略是拒绝当前请求，且抛出异常
        );
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                try {
                    System.out.println("第 - " + finalI + " -次提交的任务开始执行");
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        threadPoolExecutor.shutdown();

        /**
         * 下面是输出结果，可以看到，第0次提交的任务和第4第提交的任务都执行了，中间的第1/2次提交的任务没有执行，因为这个线程池采用的是
         */
        // 第 - 0 -次提交的任务开始执行
        //第 - 3 -次提交的任务开始执行
        //第 - 4 -次提交的任务开始执行
    }
}

