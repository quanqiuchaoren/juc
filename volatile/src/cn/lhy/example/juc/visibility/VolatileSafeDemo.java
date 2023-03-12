package cn.lhy.example.juc.visibility;

/**
 * volatile和加锁都可以保证内存可见性
 * 但是，volatile不能保证线程安全，加锁可以保证线程安全
 * 加锁保证线程安全有利有弊，因为加锁属于高开销操作
 * 所以，能用volatile解决的问题，不要用加锁的方式，比如VolatileThreadDemo.java中的例子
 */

/**
 * 演示volatile不能保证原子性的特性：会产生线程安全问题
 */
public class VolatileSafeDemo {
    public static void main(String[] args) {
        VolatileSafeThread volatileSafeThread = new VolatileSafeThread();
        for (int i = 0; i < 1000; i++) {
            new Thread(volatileSafeThread).start();
        }
    }

}

class VolatileSafeThread implements Runnable{
    private volatile int countRun = 0;
    @Override
    public void run() {
        try {
            // 用线程睡眠放大线程安全问题
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 线程序号可能会重复
         * 因为 ++countRun 不是一个原子操作
         * 分为三步：
         *      1.从主存取出countRun
         *      2.进行加一操作
         *      3.将新的值刷新到主存
         */
        System.out.println("第"+ (++countRun) +"个线程执行run方法");
    }
}

