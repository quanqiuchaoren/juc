package cn.lhy.example.juc._synchronized.wait;

/**
 * 测试对象的wait方法
 */
public class WaitTest1 {
    public static void main(String[] args) {
        String lock = new String("");
        try {
            /**
             * wait方法会强迫当前线程释放对象锁
             * 调用某个对象的wait方法时，需要提前获取该对象的同步锁，否则会抛出异常
             *      java.lang.IllegalMonitorStateException
             * 	        at java.lang.Object.wait(Native Method)
             * 	        at cn.lhy.example.juc._synchronized.wait.WaitTest2.main(WaitTest2.java:12)
             */
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
