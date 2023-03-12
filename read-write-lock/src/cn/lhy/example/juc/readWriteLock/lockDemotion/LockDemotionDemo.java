package cn.lhy.example.juc.readWriteLock.lockDemotion;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁：锁降级
 * 写锁可降级为读锁
 * 读锁不可以降级为写锁，或者说读锁不可以升级为写锁，只能放弃读锁，然后重新获取写锁
 * 写锁降级的好处，可以提高吞吐率，让其他想获取读锁的线程获取到读锁
 * 因为写锁和读锁互斥，不能同时获取
 */
public class LockDemotionDemo {
}

// 缓存数据，用于模拟写锁降级到读锁
class CacheDtaThread{
    String data;
    volatile boolean isUpdated;
    final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    public void processCacheData(String newData){
        /**
         * 如果不是最新的数据，则先更新数据
         * 先用if检查，避免直接获取写锁这种高开销操作
         */
        if(!isUpdated){
            // 更新前，获取写锁
            reentrantReadWriteLock.writeLock().lock();
            try {
                // 双重检查，确保数据确实没有被其他的线程更新
                if(!isUpdated){
                    data = newData;
                    isUpdated = true;
                }
                /**
                 * 没释放写锁前，获取读锁，然后释放写锁，称之为读写锁的降级，可以提高效率
                 * 节省了一步：重新获取读锁的开销
                 */
                reentrantReadWriteLock.readLock().lock();
            } finally {
                // 释放写锁
                reentrantReadWriteLock.writeLock().unlock();
            }
        }
        /**
         * 使用finally代码块时，可以不用catch代码块
         */
        try {
            System.out.println("最新的数据 ->" + this.data);
        } finally {
            reentrantReadWriteLock.readLock().unlock();
        }
    }
}