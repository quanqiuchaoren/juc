package cn.lhy.example.juc.readWriteLock.base;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁：在频繁读、写操作较少的情况下，可提高并发效率
 */
public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyData myData = new MyData();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> myData.getNum()).start();
        }
        for (int i = 0; i < 100; i++) {
            new Thread( () -> myData.setNum(Integer.valueOf((int)(Math.random()*100)))).start();
        }
    }
}

class MyData{
    private Integer num = 0;
    /**
     * 读写锁
     * ReentrantReadWriteLock除了无参数构造器，还有一个有参构造器
     *      ReentrantReadWriteLock(boolean fail)
     *      参数为true时：公平模式
     *      参数为false时：非公平模式，无参构造器默认调用有参数的构造器，然后传入false
     *      非公平模式比公平模式具有更高的吞吐量
     *
     */
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    public void getNum(){
        readWriteLock.readLock().lock();
        /**
         * 使用try{}finally{}时，可以不需要catch代码块
         */
        try {
            System.out.println(Thread.currentThread().getName() + "获取的数据为 -> " + this.num);
        } finally {
            // 释放锁的代码放在finally代码块中，确保锁能释放
            readWriteLock.readLock().unlock();
        }

    }

    public void setNum(Integer num) {
        readWriteLock.writeLock().lock();
        /**
         * 使用try{} finally{}的时候，可以不需要catch代码块
         */
        try{
            this.num = num;
            System.out.println(Thread.currentThread().getName() + "将数据设置为  -> " + num);
        } finally{
            readWriteLock.writeLock().unlock();
        }
    }
}