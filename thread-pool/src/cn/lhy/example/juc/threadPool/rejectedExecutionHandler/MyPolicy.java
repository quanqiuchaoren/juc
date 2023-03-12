package cn.lhy.example.juc.threadPool.rejectedExecutionHandler;

import java.util.concurrent.*;

/**
 * 实验，达到核心线程数之后，阻塞队列也已经满，有新的请求时，如何处理
 * 自己实现拒绝任务时的处理策略：每次任务被遗弃时，都会输出日志
 */
public class MyPolicy {
    public static void main(String[] args) {
        int corePoolSize = 1;
        int maximumPoolSize = 1;
        long keepAliveTime = 60;
        TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(2);
        // 采用DiscardOldestPolicy，即丢弃掉队列中最老的请求，且不会抛出异常
        MyDiscardPolicy handler = new MyDiscardPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, keepAliveTimeUnit, blockingQueue, Executors.defaultThreadFactory(), handler);
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(new MyThread(i));
        }
        threadPoolExecutor.shutdown();

        /*
         * 下面是输出结果，可以看到，每次任务被遗弃时，都会输出日志
         */
        // 有一个执行体被打印了，执行体为：这是第1次提交的任务
        //第 - 0 -次提交的任务开始执行
        //有一个执行体被打印了，执行体为：这是第2次提交的任务
        //第 - 3 -次提交的任务开始执行
        //第 - 4 -次提交的任务开始执行
    }
}

class MyThread implements Runnable {
    private final int i;

    public MyThread(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "这是第" + i + "次提交的任务";
    }

    @Override
    public void run() {
        try {
            System.out.println("第 - " + i + " -次提交的任务开始执行");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 实现拒绝任务时的处理策略：每次任务被遗弃时，都会输出日志
 */
class MyDiscardPolicy implements RejectedExecutionHandler {
    public MyDiscardPolicy() {
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            Runnable poll = e.getQueue().poll();
            System.out.println("有一个执行体被打印了，执行体为：" + poll); // 增加了一个打印功能
            e.execute(r);
        }
    }
}