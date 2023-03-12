package cn.lhy.example.juc._synchronized.reentrantLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 调用Condition对象的signal()和await()方法，都需要先获得创建Condition对象的同步锁，不然会抛出异常
 *      和普通的锁调用wait()时，需要先加锁，是一样的，都需要先获得锁
 */
public class SignalDemo2 {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        try {
            /**
             * 调用Condition的await()方法和signal()方法都需要先获得创建Condition的锁，不然会抛出异常，
             * Exception in thread "main" java.lang.IllegalMonitorStateException
             * 	at java.util.concurrent.locks.ReentrantLock$Sync.tryRelease(ReentrantLock.java:151)
             * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer.release(AbstractQueuedSynchronizer.java:1261)
             * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer.fullyRelease(AbstractQueuedSynchronizer.java:1723)
             * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2036)
             * 	at cn.lhy.example.juc._synchronized.reentrantLock.SignalDemo2.main(SignalDemo2.java:16)
             */
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
