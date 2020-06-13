package cn.lhy.example.juc.thread;

/**
 * 使用静态同步方法解决线程安全问题
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
     * Object对象内置了一把锁，就是同步锁，其他的类继承了Object对象，所以也会有一把锁
     * 同步方法想要执行，需要获得同步锁，而这把锁即是当前对象this
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
             *      所以使用System.exit(0)来让线程死亡，以此结束循环
             * 也可随意使用判断返回值的方式来让线程退出
             */
            System.exit(0);
        }
    }
}