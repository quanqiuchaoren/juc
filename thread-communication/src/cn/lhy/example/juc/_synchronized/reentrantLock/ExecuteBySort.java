package cn.lhy.example.juc._synchronized.reentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程交替执行
 * 有三个线程，线程名依次为：A、B、C
 * 要求：依次按照 A B C A B C ... A B C 的顺序输出个线程的名字，循环10次
 */
public class ExecuteBySort {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();
        Thread threadA = new Thread(new ExecuteBySortThread(lock, conditionA, conditionB), "A");
        Thread threadB = new Thread(new ExecuteBySortThread(lock, conditionB, conditionC), "B");
        Thread threadC = new Thread(new ExecuteBySortThread(lock, conditionC, conditionA), "C");
        threadA.start();
        threadB.start();
        threadC.start();
        try {
            // 让当前线程阻塞，让三个子线程先完成初始化，调用Condition的的await方法阻塞他们
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.lock();
        // 将线程A从阻塞区（等待区）移入可执行区
        conditionA.signal();
        lock.unlock();
    }
}

class ExecuteBySortThread implements Runnable {
    Lock lock;
    Condition currentCondition;
    Condition nextCondition;

    public ExecuteBySortThread(Lock lock, Condition currentCondition, Condition nextCondition) {
        this.lock = lock;
        this.currentCondition = currentCondition;
        this.nextCondition = nextCondition;
    }

    @Override
    public void run() {
        lock.lock();
        // 线程刚开始就阻塞，让主线程来决定谁先开始执行
        try {
            currentCondition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName());
            nextCondition.signal();
            if(9 != i){
                try {
                    currentCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        lock.unlock();
    }
}
