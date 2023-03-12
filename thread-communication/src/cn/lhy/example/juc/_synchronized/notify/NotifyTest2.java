package cn.lhy.example.juc._synchronized.notify;

/**
 * 测试对象的notify方法
 */
public class NotifyTest2 {
    public static void main(String[] args) {
        String lock1 = new String("");
        String lock2 = new String("");
        synchronized (lock1){
            System.out.println("synchronized lock1 enter");
            synchronized (lock2){
                System.out.println("synchronized lock2 enter");
                /**
                 * 隔着lock2的同步锁调用lock1的notify方法，不会抛出异常
                 */
                lock1.notify();
                System.out.println("synchronized lock2 out");
            }
            System.out.println("synchronized lock2 out");
        }
    }
}
