package c.lhy.example.juc.interrupt_exception;

import java.util.concurrent.TimeUnit;

public class InterruptExceptionTest {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        myThread.start();
        myThread.interrupt();
        // sleep等待2秒，等myThread运行完
        TimeUnit.SECONDS.sleep(2);
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        try {
            /*
             * 这是在睡眠时，由于主线程调用了这个线程的interrupt()方法，所以会抛出InterruptedException。
             */
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("捕获到了InterruptedException异常");
            e.printStackTrace();
        }
        System.out.println("Thread结束");
    }
}