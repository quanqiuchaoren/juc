package cn.lhy.example.juc.forkJoinPool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 用法与RecursiveAction差不多，只不过RecursiveTask有返回值
 */
public class RecursiveTaskTest {
    public static void main(String[] args) {
        /**
         * 累加1到300
         */
        SumTask sumTask = new SumTask(1, 300);
        /**
         * 使用ForkJoinPool的静态方法commonPool()也可以获取到一个ForkJointPool的实例
         */
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ForkJoinTask<Integer> submit = forkJoinPool.submit(sumTask);
        try {
            Integer sum = submit.get();
            System.out.println("1到300的总和为 -> " + sum); // 1到300的总和为 -> 45150
            // 计算器的结果也为45150
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        forkJoinPool.shutdown();

    }
}

class SumTask extends RecursiveTask<Integer>{
    // 单次执行任务，累加的数
    private static final Integer THRESHOLD = 20;
    private Integer start;
    private Integer end;

    public SumTask(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        // 如果要累加的数大于20个
        if(this.end-this.start > THRESHOLD){ // 则进行拆分
            int middle = (start + end) / 2;
            SumTask leftTask = new SumTask(start, middle);
            SumTask rightTask = new SumTask(middle + 1, end);
            leftTask.fork();
            rightTask.fork();
            Integer leftResult = leftTask.join();
            Integer rightResult = rightTask.join();
            return leftResult + rightResult;
        } else { // 否则进行累加，并返回
            int sum = 0;
            for (int i = start; i <= end; i++){
                sum += i;
            }
            return sum;
        }

    }
}