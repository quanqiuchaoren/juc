package cn.lhy.example.juc.forkJoinPool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * ForkJoinPool和ThreadPoolExecutor一样，都实现了Executor和ExecutorService接口
 * 所以，ForkJoinPool也是一种特殊的线程池
 * 但是，ForkJoinPool可以用有限的线程来讲任务拆分，让调用者只需要提交一个任务，
 * 而任务的分解交由ForkJoinPool与RecursiveAction（或者RecursiveTask）来实现
 * 需要注意的是：RecursiveAction没有返回值，而RecursiveTask有返回值
 */
public class RecursiveActionTest {
    public static void main(String[] args) {
        /**
         * ForkJoinPool(int parallelism)构造器
         * parallelism：ForkJoinPool的线程数
         * 如果不传入参数，则默认为当前操作系统的核心数
         */
        PrintAction printAction = new PrintAction(1, 300);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(printAction);
        try {
            // 线程阻塞2s，等待任务提交后，就关闭线程池
            forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 等任务执行完成后，就清理线程并关闭线程池
        forkJoinPool.shutdown();
    }
}

/**
 * 将一个大任务拆分成多个小任务，使用了分治 + 递归 的思想
 */
class PrintAction extends RecursiveAction{
    // 每个线程单次打印的个数（极限）
    private static final int THRESHOLD = 20;
    private int start;
    private int end;

    public PrintAction(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if ((this.end-this.start) < THRESHOLD){
            /**
             * if中的代码块即为将任务拆分
             */
            int middle = (start + end) / 2;
            PrintAction leftTask = new PrintAction(start, middle);
            PrintAction rightTask = new PrintAction(middle + 1, end);
            /**
             * 在RecursiveTask的实现类里面新建一个其实现类，并调用其join()方法，即为向ForkJoinPool提交一个新的子任务
             */
            leftTask.join();
            rightTask.join();
        } else {// 否则，直接打印
            for (int i = start; i <= end; i++) {
                System.out.println(Thread.currentThread().getName() + "打印了 -> " + i);
            }
        }
    }
}