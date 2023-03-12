package cn.lhy.example.juc.thread;

/**
 * 线程安全问题演示：理解静态同步方法和非静态同步方法的同步锁不同，导致线程安全问题
 */
public class WrongSynchronizedMethodDemo {
    public static void main(String[] args) {
        WrongStaticSynchronizedMethodThread synchronizedMethodThread = new WrongStaticSynchronizedMethodThread();
        for (int i = 0; i < 5; i++) {
            new Thread(synchronizedMethodThread, String.valueOf(i)).start();
        }
    }
}

class WrongStaticSynchronizedMethodThread implements Runnable{
    private static int ticketNumber = 100;
    @Override
    public void run() {
        while(true){
            /**
             * 当在循环体里面同时调用静态同步方法与非静态同步方法的时候，会出现线程安全问题
             * 因为静态同步方法的同步锁是当前类的Class对象，而非静态同步方法的同步锁是当前对象
             * 这回导致有两个线程同时进入静态同步方法和非静态同步方法，导致线程安全问题，
             *      比如，多个线程售卖了同一张票
             */
            sellTicket();
            staticSellTicket();
        }
    }
    /**
     * 静态同步方法的同步锁是当前类的Class对象
     */
    private static synchronized void staticSellTicket() {
        if (0 < ticketNumber){
            try {
                // 让线程休眠100ms，模拟其他处理操作，以此来放大线程安全问题
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"号线程，使用静态同步方法，售卖出了第" + ticketNumber +"张票");
            ticketNumber--;
        } else {
            System.out.println("静态方法卖票结束，程序退出");
            /**
             * 由于在一个方法中单独使用break不能使调用方法的死循环退出，
             *      所以使用System.exit(0)来让线程死亡，以此结束循环
             * 也可随意使用判断返回值的方式来让线程退出
             */
            System.exit(0);
        }
    }

    private synchronized void sellTicket() {
        if (0 < ticketNumber){
            try {
                // 让线程休眠100ms，模拟其他处理操作，以此来放大线程安全问题
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"号线程售卖出了第" + ticketNumber +"张票");
            ticketNumber--;
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