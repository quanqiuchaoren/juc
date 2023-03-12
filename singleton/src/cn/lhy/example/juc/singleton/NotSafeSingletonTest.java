package cn.lhy.example.juc.singleton;

import java.util.concurrent.TimeUnit;

/**
 * 饿汉式单例模式：单线程环境下安全，多线程环境下，会产生线程安全问题
 * 多个线程获取到的实例可能会不一样
 */
public class NotSafeSingletonTest {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            // 多次打印的实例的内存地址不一样，说明这个单例模式产生了线程安全问题了
            new Thread(() -> System.out.println(NotSafeSingleton.getInstance())).start();
        }
    }

}

class NotSafeSingleton {
    // 将构造器私有化，阻止外部创建其他实例
    private NotSafeSingleton(){

    }
    private static NotSafeSingleton notSafeSingleton;
    /**
     * 多线程会有线程安全问题
     */
    public static NotSafeSingleton getInstance(){
        if (null == notSafeSingleton){
            try {
                double random = Math.random();
                System.out.println(random);
                int lessThan10 = (int) (random * 10);
                System.out.println(lessThan10);
                // 使用juc包中的方法来让线程睡眠 lessThan10 秒
                // 用线程睡眠来放大线程安全问题
                TimeUnit.SECONDS.sleep(lessThan10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notSafeSingleton = new NotSafeSingleton();
        }
        return notSafeSingleton;
    }
}