package cn.lhy.example.juc._synchronized.wait;

/**
 * 在双重锁内调用两把锁的wait线程，需要如何唤醒？
 * 结论：一次只能释放一把锁，需要先notify()第一次wait()的锁，
 * 然后再第二次wait()，最后再notify()第二把锁
 */
public class DoubleWait {
    public static void main(String[] args) {
        String lock1 = new String("1");
        String lock2 = new String("2");
        synchronized (lock1) {
            synchronized (lock2) {
                try {
                    new Thread(new NotifyThread(lock1, lock2)).start();
                    /*
                     * 在加了两把锁的同步代码块内，一次性只能释放一把同步锁
                     */
                    System.out.println("主线程 交出 lock1");
                    lock1.wait();
                    System.out.println("lock1 被 notify");
                    System.out.println("主线程 交出 lock2");
                    /*
                     * 这里释放同步锁的代码，需要等待 lock1 被notify之后，才能释放
                     *      哪一把锁调用wait()方法，则释放哪一把锁
                     *      生产消费者模型，不能使用两把锁来分别唤醒消费者和生产者的原因，就是这个
                     */
                    lock2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

class NotifyThread implements Runnable {
    String lock1, lock2;

    public NotifyThread(String lock1, String lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        synchronized (lock1) {
            /**
             * 这里如果加了第二把同步锁 lock2，则不能进入同步代码块，
             *      因为主线程一次性只能释放一把锁，即 lock1
             */
            synchronized (lock2) {
                lock1.notify();
                System.out.println("NotifyThread 唤醒 lock1的阻塞线程");
            }
        }
    }
}