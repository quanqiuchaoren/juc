package cn.lhy.example.juc.threadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 实验，达到核心线程数之后，阻塞队列也已经满，有新的请求时，如何处理
 *      结论：创建新的线程，并把最新的请求交给新创建的线程
 */
public class ThreadPoolExecutorDemo_maxPoolSize {
    public static void main(String[] args) {
        int corePoolSize = 1;
        int maximumPoolSize = 2;
        long keepAliveTime = 60;
        TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, keepAliveTimeUnit, blockingQueue);
        threadPoolExecutor.execute(() -> {
            try {
                System.out.println("第 - 1 -次提交的任务开始执行");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadPoolExecutor.execute(() -> {
            try {
                System.out.println("第 - 2 - 次提交的任务开始执行");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadPoolExecutor.execute(() -> {
            System.out.println("第 - 3 - 次提交的任务开始执行");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        threadPoolExecutor.shutdown();

        // 输出结果
        // 第 - 1 -次提交的任务开始执行
        // 第 - 3 - 次提交的任务开始执行
        // 第 - 2 - 次提交的任务开始执行
    }
}

