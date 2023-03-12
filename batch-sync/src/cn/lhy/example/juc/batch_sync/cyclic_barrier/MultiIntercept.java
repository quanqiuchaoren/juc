package cn.lhy.example.juc.batch_sync.cyclic_barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 回环栅栏：用于同步一批线程
 * 测试多次拦截
 */
public class MultiIntercept {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        new R2(cyclicBarrier, "线程1").start();
        new R2(cyclicBarrier, "线程2").start();
        new R2(cyclicBarrier, "线程3").start();
    }
}

class R2 extends Thread{
    CyclicBarrier cyclicBarrier;

    public R2(CyclicBarrier cyclicBarrier, String threadName) {
        this.cyclicBarrier = cyclicBarrier;
        super.setName(threadName);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " - 启动了");
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("所有线程已经准备就绪 - " + Thread.currentThread().getName() + " - 启动了");
        System.out.println(Thread.currentThread().getName() + " - 到达第一个集合点");

        /**
         * cyclicBarrier.getNumberWaiting()可以获取当前有几个线程在等待
         *      即在线程中调用cyclicBarrier.await()
         */
        int numberWaiting = cyclicBarrier.getNumberWaiting();
        System.out.println("当前有" + numberWaiting + "个线程在等待");
        /**
         * cyclicBarrier.getParties()是获取总共需要等待的线程数
         *      即：需要cyclicBarrier.getParties()个线程调用cyclicBarrier.await()
         */
        int parties = cyclicBarrier.getParties();
        int waitingNum = parties - numberWaiting;
        System.out.println("还差" + waitingNum + "个线程");

        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("都到达了第一个集合点，去终点");

        System.out.println(Thread.currentThread().getName() + " - 到达了终点");

        int secondNumberWaiting = cyclicBarrier.getNumberWaiting();
        System.out.println("终点处，当前有" + numberWaiting + "个线程在等待");
        int secondWaitingNum = parties - secondNumberWaiting;
        System.out.println("终点处，还差" + secondWaitingNum + "个线程");

        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("所有线程都到终点了，坐车回家。。。");

    }
}