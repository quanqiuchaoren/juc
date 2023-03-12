package cn.lhy.example.juc._synchronized.notify;

/**
 * 测试对象的wait方法
 */
public class NotifyTest1 {
    public static void main(String[] args) {
        // 第一个对象
        String lock1 = new String("");
        // 第二个对象
        String lock2= new String("");
        synchronized (lock1){
            System.out.println("synchronized enter");
            /**
             * 与wait方法一样，当调用一个对象的notify方法时，需要提前获取该对象的同步锁
             * 此处由于提前获取到的是lock1的同步锁，但是调用notify方法的却是lock2对象，
             * 所以会抛出异常
             *      Exception in thread "main" java.lang.IllegalMonitorStateException
             */
            lock2.notify();
            System.out.println("synchronized out");
        }
    }
}
