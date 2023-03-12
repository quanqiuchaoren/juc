package cn.lhy.example.juc.visibility;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * java.utl.concurrent.atomic包是jdk为我们提供的用于多线程开发中可能会用到的类
 * atomic：原子，这个包中的类利用CAS保证原子性，是利用了硬件命令CMPXCHG保证操作的原子性的
 *      所以相较于volatile，可以保证操作的原子性，相较于加锁，开销更低
 * AtomicInteger就是其中的一个类，是对int类型的数据的封装，能保证内存可见性和原子性
 */
public class AtomicIntegerDemo {
    public static void main(String[] args) {
        AtomicIntegerThread atomicIntegerThread = new AtomicIntegerThread();
        for (int i = 0; i < 100000; i++) {
            new Thread(atomicIntegerThread).start();
        }
    }
}

class AtomicIntegerThread implements Runnable{
    /**
     * atomic包中原子类中包装的值，使用了volatile来修饰，可以保证内存可见性
     */
    private AtomicInteger countRun = new AtomicInteger(0);

    @Override
    public void run() {
        try {
            // 线程阻塞100ms，以此来放大线程安全问题
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * AtomicInteger属于原子类，原子类里面的操作都可以保证原子性，所以incrementAndGet()方法的取值、加一、刷新主存操作可以保证原子性
         * 原子类是通过CAS算法来保证原子性的，是通过硬件命令保证原子性，属于乐观锁，和传统的悲观锁（同步代码块、同步方法、重入锁）有着本质区别
         * 悲观锁：假设不会冲突，一直往前执行，刷新主存前会将主存中的现在的共享变量的值与操作前感知到的值进行比较，如果相同，则刷新（操作成功），
         *      如果不相同，则本次操作失败，重试
         */
        System.out.println("第"+countRun.incrementAndGet()+"个线程的run方法被执行");
    }
}