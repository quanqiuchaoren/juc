package cn.lhy.example.juc.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁
 *      悲观锁
 *      lock()与unlock()中的代码为临界区，临界区中的操作属于原子操作，要么一起成功，要么一起失败
 *
 * 当线资源（即有大量的线程同时竞争同步锁）竞争激烈时程很多时，重入锁的性能会远远高于同步代码块（synchronized）、同步方法
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        ReentrantLockThread reentrantLockThread = new ReentrantLockThread();
        for (int i = 0; i < 5; i++) {
            new Thread(reentrantLockThread, String.valueOf(i)).start();
        }
    }
}

class ReentrantLockThread implements Runnable{
    private int ticketNumber = 100;
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true){
            lock.lock();
            if(0 < ticketNumber){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"号线程售卖了第" + ticketNumber-- + "张票");
            } else {
                System.out.println(Thread.currentThread().getName() + "号线程卖票结束");
                // 提前释放锁，结束死循环，让其他线程也可以进入代码块来判断票是否卖完了，从而正常退出，避免程序一直不退出
                lock.unlock();
                break;
            }
            lock.unlock();
        }

        /**
         * 这种事错误写法
         */
//        while (true){
//            lock.lock();
//            if(0 < ticketNumber){
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(Thread.currentThread().getName()+"号线程售卖了第" + ticketNumber-- + "张票");
//            } else {
                  // 在临界区里面做break操作，会导致重入锁得不到释放，有几个线程一直处于阻塞状态，所以程序不会结束
                  // 需要与同步代码块和同步方法区分开来
//                break;
//            }
//            lock.unlock();
//        }
    }
}