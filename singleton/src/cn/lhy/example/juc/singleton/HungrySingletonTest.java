package cn.lhy.example.juc.singleton;

import java.util.concurrent.TimeUnit;

/**
 * 饿汉式单例：不存在线程安全问题
 * 因为类在被使用的时候就进行初始化，因为虚拟机会保证类的<clinit>方法在初始化的时候被正确的加锁、同步
 * 如果多个线程使用类，name只会有一个线程去执行这个类的<clinit>方法
 */
public class HungrySingletonTest {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> System.out.println(HungrySingleton.getInstance())).start();
        }
    }
}

class HungrySingleton{
    // 在类被加载的时候，就创建类的实例
//    private static HungrySingleton hungrySingleton = new HungrySingleton();
    private static HungrySingleton hungrySingleton;
    static {
        System.out.println("开始进行类的初始化");
        try {
            int lessThan10 = (int) (Math.random() * 10);
            System.out.println(lessThan10);
            TimeUnit.SECONDS.sleep(lessThan10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hungrySingleton = new HungrySingleton();
        System.out.println("结束类的初始化");
    }

    public static HungrySingleton getInstance(){
        return hungrySingleton;
    }
}