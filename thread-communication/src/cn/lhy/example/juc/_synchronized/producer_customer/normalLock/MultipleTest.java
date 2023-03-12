package cn.lhy.example.juc._synchronized.producer_customer.normalLock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * 线程通信实例：多个生产者和消费者
 * 使用notify()和notifyAll()来进行线程通信
 */
public class MultipleTest {
    public static void main(String[] args) {
        String lock = new String("");
        for (int i = 0; i < 5; i++) {
            new MultiProducerThread(lock, String.valueOf(i)).start();
            new MultiCustomerThread(lock, String.valueOf(i)).start();
        }
    }
}

class MultiMarcket{
    // 使用Queue（队列）模拟可以存储多个商品的场所，LinkedList实现了Queue的接口
    static public Queue<Integer> products = new LinkedList<>();
}

// 模拟生产者
class MultiProducerThread extends Thread{
    /**
     * 用静态productNum来记录所有的生产者生产的商品，如果是非静态的变量，
     * 且每个线程都使用的是不同的Target的话，会导致生产重复序号的商品
     */
    private static int productNum;
    static {
        productNum = 0;
    }
    private String lock;
    public MultiProducerThread(String lock, String threadName){
        this.lock = lock;
        super.setName(threadName);
    }
    @Override
    public void run(){

        while (true){
            synchronized(lock){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 限制队列中存储的商品的数量，5个即为满仓
                if (MultiMarcket.products.size() >= 5){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    MultiMarcket.products.offer(productNum);
                    System.out.println("生产者 - " + super.getName() + " - 生产了商品 - " + (productNum++) + " - 商品存量：" + MultiMarcket.products.size());
                    /**
                     * 为了避免使用lock.notify()唤醒的是同类线程，导致进程假死
                     * 使用notify()方法，可能是导致程序停顿，因为唤醒的可能是同类线程，在此处即为生产者线程
                     * 所以采用lock.notifyAll()来唤醒所有的生产者和消费者线程
                     * 采用两把锁的形式来避免此类问题：即生产者锁和消费者锁
                     */
                    lock.notifyAll();
                }
            }
        }
    }
}

// 模拟消费者
class MultiCustomerThread extends Thread{
    private String lock;
    public MultiCustomerThread(String lock, String threadName){
        this.lock = lock;
        super.setName(threadName);
    }
    @Override
    public void run(){
        while (true){
            synchronized(lock){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(0 == MultiMarcket.products.size()){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("消费者 - " + super.getName() +" - 消费了商品 - " + MultiMarcket.products.poll() + " - 商品存量：" + (MultiMarcket.products.size()));
                    lock.notifyAll();
                }
            }
        }
    }
}