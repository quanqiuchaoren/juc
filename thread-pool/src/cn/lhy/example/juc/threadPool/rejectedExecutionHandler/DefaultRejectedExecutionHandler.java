package cn.lhy.example.juc.threadPool.rejectedExecutionHandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 实验，达到最大线程数之后，阻塞队列也已经满，有新的请求时，如何处理
 * 默认的策略：AbortPolicy，中断提交任务到线程池，并抛出运行时异常RejectedExecutionException
 */
public class DefaultRejectedExecutionHandler {
    public static void main(String[] args) {
        int corePoolSize = 1;
        int maximumPoolSize = 1;
        long keepAliveTime = 60;
        TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, keepAliveTimeUnit, blockingQueue);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                try {
                    System.out.println("第 - " + finalI + " -次提交的任务开始执行");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("第 - " + finalI + " -次提交的任务执行完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 这后面的语句不会输出，因为有异常抛出了
        System.out.println("任务提交完毕，shutdown前");
        threadPoolExecutor.shutdown();
        System.out.println("任务提交完毕，shutdown后");

        // 第 - 0 -次提交的任务开始执行
        // 下面是抛出的异常
        //Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task cn.lhy.example.juc.threadPool.rejectedExecutionHandler.RejectedExecutionHandler$$Lambda$1/1078694789@448139f0 rejected from java.util.concurrent.ThreadPoolExecutor@7cca494b[Running, pool size = 1, active threads = 1, queued tasks = 1, completed tasks = 0]
        //	at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2063)
        //	at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:830)
        //	at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1379)
        //	at cn.lhy.example.juc.threadPool.rejectedExecutionHandler.RejectedExecutionHandler.main(RejectedExecutionHandler.java:29)
        //第 - 0 -次提交的任务执行完毕
        //第 - 1 -次提交的任务开始执行
        //第 - 1 -次提交的任务执行完毕
    }
}

