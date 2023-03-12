package cn.lhy.example.juc._synchronized.wait;

import java.util.concurrent.TimeUnit;

/**
 * 测试：调用wait方法之后，剩余的代码是否还执行
 *
 * 结论：会重新执行，在被notify()之后，可以被JVM重新调用，如果获得了锁，则会继续执行
 */
public class RestSynchronizedBlock {

    public static void main(String[] args) {
        String lock = new String("");
        new Thread(new RestThread(lock)).start();
        try {
            /**
             * 主线程沉睡，让子线程先调用wait方法
             */
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock){
            System.out.println("主 - 线程加锁代码块");
            /**
             * 因为使用notify方法之前，需要先获得同步锁，所以在同步代码块中将子线程从等待区移入可执行区
             */
            lock.notify();
        }

    }
}


class RestThread implements Runnable{
    String lock;

    public RestThread(String lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock){
            System.out.println("在 - 子 - 线程中加锁之后");
            System.out.println("在 - 子 - 线程中调用wait方法");
            try {
                /**
                 * 子线程调用wait方法，交出同步锁
                 */
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /**
             * 这里的代码会输出，在本子线程重新从等待区移入可执行队列之后，可被进程（JVM）重新被调度
             */
            System.out.println("子线程在wait方法之后的代码");
        }
    }
}