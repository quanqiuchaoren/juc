package cn.lhy.example.juc._synchronized.producer_customer.reentrantLockAndCondition.single_producer_customer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用重入锁来进行线程通信单独的一个生产者和消费者进行协作
 */
public class SinglePC {
    public static void main(String[] args) {
        MyReEntrantLock lock = new MyReEntrantLock();
        new Thread(new Producer(lock)).start();
        new Thread(new Customer(lock)).start();
    }
}

class Producer implements Runnable{
    MyReEntrantLock lock;

    public Producer(MyReEntrantLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        for(int i=0; true; ){
            lock.lock.lock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (null != Marcket.product){
                try {
                    lock.condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Marcket.product = String.valueOf(i++);
                System.out.println("生产者生产了货物 -> " + Marcket.product);
                lock.condition.signal();
            }
            lock.lock.unlock();
        }
    }
}

class Customer implements Runnable{
    MyReEntrantLock lock;

    public Customer(MyReEntrantLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true){
            lock.lock.lock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (null == Marcket.product){
                try {
                    lock.condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("消费者消费了货物 -> " + Marcket.product);
                Marcket.product = null;
                lock.condition.signal();
            }
            lock.lock.unlock();
        }
    }
}

class Marcket{
    static String product;
}

// 生产者和消费者之间的线程锁
class MyReEntrantLock{
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
}