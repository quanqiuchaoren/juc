package cn.lhy.example.juc.batch_sync.cyclic_barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 回环栅栏：用于同步一批线程
 *      可以进行无限次同步
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        Runnable r = new R1(cyclicBarrier);
        new Thread(r, "1").start();
        new Thread(r, "2").start();
        new Thread(r, "3").start();
    }
}

class R1 implements Runnable{
    CyclicBarrier cyclicBarrier;

    public R1(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println("线程" + Thread.currentThread().getName() + "启动了");
        try {
            for(int i=0; ; i++){
                TimeUnit.MILLISECONDS.sleep(2000);
                cyclicBarrier.await();
                System.out.println("栅栏第" + i + "次被放开，" + "线程" + Thread.currentThread().getName() + "启动了");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

    }
}