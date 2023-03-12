package cn.lhy.example.juc.batch_sync.cyclic_barrier;

import java.time.LocalTime;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {
    public static void main(String[] args) {
        int N = 8;            //工人数
        Semaphore semaphore = new Semaphore(5); //机器数目
        for (int i = 0; i < N; i++)
            new Worker(i, semaphore).start();
    }

    static class Worker extends Thread {
        private int num;
        private Semaphore semaphore;

        public Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    semaphore.acquire();
                    System.out.println(LocalTime.now() + "工人" + this.num + "占用一个机器在生产...");
                    TimeUnit.MINUTES.sleep(1);
                    System.out.println(LocalTime.now() + "工人" + this.num + "释放出机器");
                    semaphore.release();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}