package cn.lhy.example.juc.exchanger;

import java.util.concurrent.Exchanger;

/**
 * 线程交换器：阻塞两个线程，等待数据交换完成，才可以继续执行
 */
public class ExchangerTest {
    private static String ideaA;
    private static String ideaB;
//    private Exchanger<String> exchanger;
    void changeOfA(Exchanger<String> exchanger){
        ideaA = "我是思想A";
        System.out.println("ideaA -> " + ideaA);
        try {
            String exchangeResult = exchanger.exchange(ideaA);
            System.out.println("线程A从目标线程获取的信息是 -> " + exchangeResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    void changeOfB(Exchanger<String> exchanger){
        ideaB = "我是思想B";
        System.out.println("ideaB -> " + ideaB);
        try {
            String exchangeResult = exchanger.exchange(ideaB);
            System.out.println("线程B从目标线程获取到的信息是 -> " + exchangeResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final ExchangerTest exchangerTest = new ExchangerTest();
        final Exchanger<String> exchanger = new Exchanger<>();
        /**
         * 如果主线程在这里等待交换，则会发生死锁，因为没有其他线程与主线程进行信息交换
         * 因为主线程阻塞了，后面的线程无法启动，与主线程进行信息交换
         */
        // exchangerTest.changeOfA(exchanger);
        new Thread(() -> exchangerTest.changeOfA(exchanger)).start();
        new Thread(() -> exchangerTest.changeOfB(exchanger)).start();
    }
}
