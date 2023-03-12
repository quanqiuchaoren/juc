package cn.lhy.example.juc.batch_sync.cyclic_barrier.count_down_latch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 计数器锁：当计数达到规定时，释放锁
 */
public class BaseDemo {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(10);
        new Thread(new CountdownThread(latch)).start();
        new Thread(new CountdownThread(latch)).start();
        new Thread(new AwaitThread(latch)).start();
        System.out.println("主线程即将发生等待");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程被唤醒");
        /**
         * 再次创建线程，测试是否被阻塞，发现没有被阻塞
         *      说明CountDownLatch的getCount()方法只要是返回0了，
         *      则调用该锁的await()方法，就不能阻塞线程了
         */
        new Thread(new AwaitThread(latch)).start();
    }
}

class CountdownThread implements Runnable{
    private CountDownLatch latch;
    public CountdownThread(CountDownLatch latch){
        this.latch = latch;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("latch.getCount() - " + latch.getCount());
            // 将计数器锁的计数加一
            latch.countDown();
        }
    }
}

class AwaitThread implements Runnable{
    private CountDownLatch latch;
    public AwaitThread(CountDownLatch latch){
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println("await线程即将发生等待");
            latch.await();
            System.out.println("await线程被唤醒了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}