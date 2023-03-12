package jstack;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用jstack查看死锁
 */
public class DeadLock {
    public static void main(String[] args) {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        Thread thread_lebron = new Thread(() -> {
            lock1.lock();
            System.out.println(Thread.currentThread().getName() + "获得lock1");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.lock();
            System.out.println(Thread.currentThread().getName() + "获得lock2");
            lock2.unlock();
            lock1.unlock();
        });
        Thread thread_davis = new Thread(() -> {
            lock2.lock();
            System.out.println(Thread.currentThread().getName() + "获得了lock2");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.lock();
            System.out.println(Thread.currentThread().getName() + "获得了lock1");
            lock1.unlock();
            lock2.unlock();
        });
        thread_lebron.setName("lebron");
        thread_davis.setName("davis");
        thread_lebron.start();
        thread_davis.start();
    }
}
