package cn.lhy.example.juc._synchronized.notify;

import java.util.concurrent.TimeUnit;

/**
 * 测试：当调用了锁的notify()后，会马上释放锁吗？
 *      结论：notify()方法不会释放当前锁，会一直执行下去
 */
public class NotifyTest3 {
    public static void main(String[] args) {
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock){
                try {
                    lock.wait();
                    System.out.println("t1被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(lock){
            lock.notify();
            System.out.println("main已经唤醒了t1");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("main沉睡结束");
        }
    }
}
