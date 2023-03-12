package cn.lhy.example.juc._synchronized.producer_customer.normalLock;

/**
 * 线程通信实例：一个生产者线程和一个消费者线程互动
 * 使用wait()、notify()来进行线程通信
 */
public class SimpleTest {
    public static void main(String[] args) {
        String lock = new String("");
        new ProducerThread(lock).start();
        new CustomerThread(lock).start();
    }

}

// 模拟存放数据的场所
class Marcket {
    public static String product;
}

// 模拟生产者
class ProducerThread extends Thread{
    private String lock;
    public ProducerThread(String lock){
        this.lock = lock;
    }
    @Override
    public void run(){
        // 用10个循环来模拟生产10个商品
        for (int i = 0; i < 10; i++) {
            synchronized(lock){
                if(null != Marcket.product){
                    try {
                        /**
                         * 调用某一个对象的wait方法时，能阻塞当前线程，也就是正在执行的线程
                         * 会让当前线程释放同步锁，这个地方即lock，让消费者可以进入到同步代码块中消费商品
                         * 消费者消费了商品，调用，lock.notify()的时候，
                         * 本线程（生产者）就可以从阻塞状态进入可执行状态，进入线程池的“可执行”队列，等待执行
                         * wait方法来自于Object，是native方法，底层是调用的c语言或者c++语言编写的代码
                         */
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String newProduct = "商品" + i;
                System.out.println("生产数据，结果 ->" + newProduct);
                Marcket.product = newProduct;
                /**
                 * 唤醒一个等待锁的的线程，即调用了object.wait()的线程
                 * 由于只有一个消费者和一个生产者，所以这里即为唤醒消费者
                 */
                lock.notify();
            }

        }

    }
}

// 模拟消费者
class CustomerThread extends Thread{
    private String lock;
    public CustomerThread(String lock){
        this.lock = lock;
    }
    @Override
    public void run(){
        // 用死循环来一直消费商品
        while (true){
            synchronized (lock){
                if(null == Marcket.product){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("消费的数据 -> " + Marcket.product);
                // 实际消费数据置空（消费）
                Marcket.product = null;
                lock.notify();
            }
        }

    }
}
