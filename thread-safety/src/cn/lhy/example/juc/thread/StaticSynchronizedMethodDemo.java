package cn.lhy.example.juc.thread;

/**
 * 使用静态同步方法解决线程安全问题：多个线程卖票问题
 */
public class StaticSynchronizedMethodDemo {
    public static void main(String[] args) {
        StaticSynchronizedMethodThread synchronizedMethodThread = new StaticSynchronizedMethodThread();
        for (int i = 0; i < 5; i++) {
            new Thread(synchronizedMethodThread, String.valueOf(i)).start();
        }
    }
}

class StaticSynchronizedMethodThread implements Runnable{
    private static int ticketNumber = 100;
    @Override
    public void run() {
        while(true){
            sellTicket();
        }
    }
    /**
     * 静态方法的同步锁：即当前类的Class对象
     */
    private static synchronized void  sellTicket() {
        if (0 < ticketNumber){
            try {
                // 让线程休眠100ms，模拟其他处理操作，以此来放大线程安全问题
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"号线程售卖出了第" + ticketNumber-- +"张票");
        } else {
            System.out.println("卖票结束，程序退出");
            /**
             * 由于在一个方法中单独使用break不能使调用方法的死循环退出，
             *      所以使用System.exit(0)来让进程死亡，以此结束循环
             * 也可随意使用判断返回值的方式来让线程退出
             */
            System.exit(0);
        }
    }
}