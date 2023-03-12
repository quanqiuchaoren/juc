package cn.lhy.example.juc._synchronized.reentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试：让signal()方法唤醒指定的线程
 */
public class SignalDemo {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        new Thread(new Runnable(){
            public void run(){
                try {
                    lock.lock();
                    condition1.await();
                    lock.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("condition1阻塞的线程被唤醒了，重新进入了可执行队列，并被JVM调度执行了");
            }
        }).start();
        new Thread(new Runnable(){
            public void run(){
                try{
                    lock.lock();
                    condition2.await();
                    lock.unlock();
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("condition2阻塞的线程被唤醒了，重新进入了可执行队列，并被JVM调度执行了");
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.lock();
        /**
         * 在condition2唤醒了由condition2阻塞的线程后，
         *      System.out.println("condition2阻塞的线程重新进入了可执行队列，并被JVM调度执行了");
         *      会执行
         * condition1阻塞的线程不会被唤醒
         *      因为condition对象只能唤醒由自己阻塞的线程，
         *      所以，用condition实现的多个生产者与消费者模型，会比普通的锁对象实现的效率更高，
         *              因为condition可以实现唤醒指定类的线程，在生产者、消费者模型中，
         *              即为消费者唤醒生产者，生产者唤醒消费者；
         *              而普通的锁对象执行notify()方法唤醒的是所有的线程，包括同类线程，
         *              可能会导致程序短暂暂停
         */
        condition2.signal();
        lock.unlock();
        /**
         * 程序不会结束，因为线程1一直阻塞，没有被唤醒
         */
    }
}
