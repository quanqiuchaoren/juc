package cn.lhy.example.juc._synchronized.wait;

import java.time.LocalTime;

/**
 * 测试对象的wait(long millis)方法
 * 超时过后，自动苏醒
 */
public class WaitTest2 {
    public static void main(String[] args) {
        String lock = new String("");
        synchronized (lock) {
            System.out.println(LocalTime.now() + "synchronized enter");
            try {
                lock.wait(5 * 1000);
                System.out.println("同步代码块中，wait()之后的代码");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(LocalTime.now() + "synchronized out");
        }
    }
}
