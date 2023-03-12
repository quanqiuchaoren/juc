package cn.lhy.example.juc._synchronized.reentrantLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程，线程名字依次为A、B、C
 * 将这三个线程的名字依次打印在屏幕上10次
 *      ABCABC。。。，依次递归
 */
public class HuaweiTest {
    public static void main(String[] args){
        Lock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();
        int[] serialPromise = {0};
        new PrintThread("A", lock, conditionA, conditionB, serialPromise, 0).start();
        new PrintThread("B", lock, conditionB, conditionC, serialPromise, 1).start();
        new PrintThread("C", lock, conditionC, conditionA, serialPromise, 2).start();
    }
}

class PrintThread extends Thread{
    private Lock lock;
    private Condition condition;
    private Condition nextCondition;
    int[] pro;
    int posi;
    public PrintThread(String name, Lock lock, Condition condition, Condition nextCondition, int[] pro, int posi) {
        super(name);
        this.lock = lock;
        this.condition = condition;
        this.nextCondition = nextCondition;
        this.pro = pro;
        this.posi = posi;
    }
    @Override
    public void run() {
        super.run();
        try {
            lock.lock();
            if (pro[0]%3 != posi){
                // System.out.println("线程" + this.getName() + "被阻塞");
                condition.await();
                // System.out.println("线程" + this.getName() + "被唤醒");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        for(int i=0; i<10; i++){
            try{
                lock.lock();
                System.out.println(this.getName());
                pro[0] += 1;
                nextCondition.signal();
                if (i != 9){
                    condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                lock.unlock();
            }
        }
    }
}