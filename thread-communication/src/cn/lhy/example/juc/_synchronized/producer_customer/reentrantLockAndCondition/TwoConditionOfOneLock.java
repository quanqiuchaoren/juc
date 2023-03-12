package cn.lhy.example.juc._synchronized.producer_customer.reentrantLockAndCondition;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用重入锁来获取Condition，用Condition来对锁进行放弃操作
 * 用Condition来唤醒指定异类线程，避免唤醒同类线程，让程序陷入短暂停滞
 *      因为Condition可以实现分组唤醒线程
 */
public class TwoConditionOfOneLock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition producerCondition = lock.newCondition();
        Condition customerCondition = lock.newCondition();
        Runnable producer = new Producer(lock, producerCondition, customerCondition);
        Runnable customer = new Customer(lock, producerCondition, customerCondition);
        for (int i = 0; i < 5; i++) {
            new Thread(producer, "生产者"+i).start();
            new Thread(customer, "消费者"+i).start();
        }
    }
}

class Marcket{
    static final Queue<Integer> products = new LinkedList<>();
}

class Producer implements Runnable{
    Lock lock;
    Condition producerCondition;
    Condition customerCondition;
    int productNo = 0;

    public Producer(Lock lock, Condition producerCondition, Condition customerCondition) {
        this.lock = lock;
        this.producerCondition = producerCondition;
        this.customerCondition = customerCondition;
    }

    @Override
    public void run() {
        while (true){
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Marcket.products.size() >= 5){
                try {
                    producerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Marcket.products.offer(productNo);
                System.out.println(Thread.currentThread().getName() + " - 生产了商品 -> " + productNo++
                        + "商品剩余数量 - " + Marcket.products.size());
            }
            customerCondition.signal();
            lock.unlock();
        }
    }
}

class Customer implements Runnable{
    Lock lock;
    Condition producerCondition;
    Condition customerCondition;

    public Customer(Lock lock, Condition producerCondition, Condition customerCondition) {
        this.lock = lock;
        this.producerCondition = producerCondition;
        this.customerCondition = customerCondition;
    }

    @Override
    public void run() {
        while (true){
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Marcket.products.size() == 0){
                try {
                    customerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " - 消费了商品 -> " + Marcket.products.poll()
                + "商品剩余数量 - " + Marcket.products.size());
            }
            producerCondition.signal();
            lock.unlock();
        }
    }
}