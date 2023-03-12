package c.lhy.example.juc.interrupt;

import java.util.concurrent.TimeUnit;

public class InterruptTest {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        myThread.start();
        myThread.interrupt();
        //sleep等待一秒，等myThread运行完
        TimeUnit.SECONDS.sleep(2);
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("i=" + (i + 1));
            if (this.isInterrupted()) {
                System.out.println("通过this.isInterrupted()检测到中断"); // 通过this.isInterrupted()检测到中断
                System.out.println("第一个interrupted()：" + Thread.interrupted()); // 第一个interrupted()：true
                System.out.println("第二个interrupted()：" + Thread.interrupted()); // 第二个interrupted()：false
                break;
            }
        }
        System.out.println("因为检测到中断，所以跳出循环，线程到这里结束，因为后面没有内容了");
        // 因为检测到中断，所以跳出循环，线程到这里结束，因为后面没有内容了
    }
}