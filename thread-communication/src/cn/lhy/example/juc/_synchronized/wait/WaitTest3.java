package cn.lhy.example.juc._synchronized.wait;

/**
 * 测试对象的wait方法
 *      如果调用lock.wait()，则需要先对lock进行加锁，即对特定的锁加锁才行
 */
public class WaitTest3 {
    public static void main(String[] args) {
        /**
         * 即使字符串的equals方法比较相同，只要不是同一个对象，则lock与lock2也不是同一把锁
         * 如果是以下形式，则lock与lock2是同一把锁，因为是对同一块堆内存的引用
         *      String lock = "";
         *      String lock2 = "";
         */
        String lock = new String("");
        String lock2 = new String("");
        synchronized (lock){
            try {
                /**
                 * 使用某个对象的wait方法时，需要提前获取该对象的同步锁
                 * 如果加锁使用的是lock，而调用wait方法时，使用的是lock2，则会抛出异常
                 *      java.lang.IllegalMonitorStateException
                 *           at java.lang.Object.wait(Native Method)
                 *           at cn.lhy.example.juc._synchronized.wait.WaitTest2.main(WaitTest2.java:12)
                 */
                lock2.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("synchronized out");
        }
    }
}
