package cn.lhy.example.juc._synchronized.wait;

import java.util.concurrent.TimeUnit;

/**
 * 测试：调用wait方法，被唤醒之后，重新从等待区进行可执行队列中时，
 *      执行synchronized代码块中剩余的代码时，是否重新获取了同步锁
 * 结论：重新获取了同步锁
 * 其实也很好理解，因为这些代码本来就处在同步代码块中，如果没有重新获取锁，会给人造成误解，
 *      让人理解起来很困难，造成“执行同步代码块中的代码没有获取锁？？？？（尼克杨问号脸。。。）”
 * 其实钟宏发的笔记中也有讲，线程t1中调用lock1的wait()，线程t2将用lock1将t1唤醒，
 *      所以此时，t1处于阻塞状态，因为调用wait()的时候，是处于同步代码块当中的
 *      此时，t2肯定还是在同步代码块之中的，当t2的同步代码块执行完毕之后，如果t1重新获得了lock1，则继续执行
 */
public class GetLockAfterWait {

    public static void main(String[] args) {
        String lock = new String("");
        new Thread(new GetLockThread(lock)).start();
        // 主线程沉睡1秒，让子线程先调用wait方法
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock){
            System.out.println("在 - 主 - 线程中加锁之后");
            // 唤醒子线程
            lock.notify();
        }
        long startMillis = System.currentTimeMillis();
        // 再次沉睡，让子线程执行
        try {
            System.out.println("主线程沉睡1秒，让回到可执行队列的子线程先执行");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock){
            long endMillis = System.currentTimeMillis();
            long secondsCosted = (endMillis - startMillis)/1000;
            System.out.println("主线程在" + secondsCosted + "秒之后，重新获得锁");
            if(secondsCosted > 2){
                System.out.println("主线程重新获得锁的时间超过2s，实际时间为" + secondsCosted + "秒");
                System.out.println("说明子线程从等待区回到可执行队列之后，" +
                        "重新执行synchronized代码块中，wait方法之后的代码时，是重新获取了同步锁的" +
                        "即使在之前调用wait方法的时候交出了同步锁");
            }
        }

    }
}


class GetLockThread implements Runnable{
    String lock;

    public GetLockThread(String lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock){
            System.out.println("在 - 子 - 线程中加锁之后");
            System.out.println("在 - 子 - 线程中调用wait方法");
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子 - 线程wait()结束，重新运行");
            System.out.println("子 - 线程开始睡眠");
            try {
                /**
                 * 子线程沉睡5秒，检测主线程是否会执行，一次来推断子线程重新执行只有，是否重新获得了锁
                 */
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("睡眠结束");
        }
    }
}