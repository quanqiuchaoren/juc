package cn.lhy.example.juc._synchronized.producer_customer.normalLock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * 线程通信实例：多个生产者和消费者
 * 使用notify()和notify()来进行线程通信
 * 使用两把锁
 * 最终，证明不可行，因为notify和wait方法均需要提前获得调用对象的同步锁
 *      如果生产者和消费者都提前获取两把同步锁，但是只能释放一把锁，最后就会导致死锁
 */
public class MultipleOf2LockTest {
    public static void main(String[] args) {
        String producerLock = new String("1");
        String customerLock = new String("2");
        for (int i = 0; i < 5; i++) {
            new MultiOf2LockProducerThread(producerLock, customerLock, String.valueOf(i)).start();
            new MultiOf2LockCustomerThread(producerLock, customerLock, String.valueOf(i)).start();
        }
    }
}

// 模仿市场的队列，用来存放商品
class MultiOf2LockMarcket {
    // 使用Queue（队列）模拟可以存储多个商品的场所，LinkedList实现了Queue的接口
    static public Queue<Integer> products = new LinkedList<>();
}

// 模拟生产者
class MultiOf2LockProducerThread extends Thread {
    private static int productNum;

    static {
        productNum = 0;
    }

    private String producerLock;
    private String customerLock;

    public MultiOf2LockProducerThread(String producerLock, String customerLock, String threadName) {
        this.producerLock = producerLock;
        this.customerLock = customerLock;
        super.setName(threadName);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (producerLock) {
                synchronized (customerLock) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /**
                     * 为了避免使用lock.notify()唤醒的是同类线程，导致进程假死
                     * 所以采用lock.notifyAll()来唤醒所有的生产者和消费者线程
                     * 使用notifyAll()方法，可能是导致程序停顿，因为唤醒的可能是同类线程，在此处即为生产者线程
                     * 采用两把锁的形式来避免此类问题：即生产者锁和消费者锁
                     */
                    // 限制队列中存储的商品的数量，5个即为满仓
                    if (MultiOf2LockMarcket.products.size() >= 5) {
//                        customerLock.notify();
                        try {
//                            customerLock.wait();
                            producerLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        MultiOf2LockMarcket.products.offer(productNum);
                        System.out.println("生产者 - " + super.getName() + " - 生产了商品 - " + (productNum++) + " - 商品存量:" + MultiOf2LockMarcket.products.size());
//                        customerLock.notify();
                    }
                    customerLock.notify();
                }
            }
        }
    }
}

// 模拟消费者
class MultiOf2LockCustomerThread extends Thread {
    String producerLock;
    String customerLock;

    public MultiOf2LockCustomerThread(String producerLock, String customerLock, String threadName) {
        this.producerLock = producerLock;
        this.customerLock = customerLock;
        super.setName(threadName);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (producerLock) {
                synchronized (customerLock) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (0 == MultiOf2LockMarcket.products.size()) {
                        try {
                            customerLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("消费者 - " + super.getName() + " - 消费了商品 - " + MultiOf2LockMarcket.products.poll() + " - 商品存量" + (MultiOf2LockMarcket.products.size()));
//                        producerLock.notify();
                    }
                    producerLock.notify();
                }
            }
        }
    }
}