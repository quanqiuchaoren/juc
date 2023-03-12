package cn.lhy.example.juc._synchronized.lock_support;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport的基础用法
 */
public class BaseDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                LockSupport.park();
                System.out.println("线程1被第一次发放通过许可");
                LockSupport.park();
                System.out.println("线程1被第二次发放许可");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        System.out.println("主线程即将给 - 线程1 - 发放第一张许可");
        /**
         * unpark(t1)只能存储一个,所以要等线程t1消费了之后,才能继续发放许可
         *      所以,下面的睡眠3秒是为了等线程t1将unpark(t1)消费掉
         */
        LockSupport.unpark(t1);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程即将给 - 线程1 - 发放第二章许可");
        LockSupport.unpark(t1);
    }
}
