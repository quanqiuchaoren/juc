package cn.lhy.example.juc.thread;

/**
 * 线程安全问题典型案例：多个线程售卖同一批票
 * 会出现线程安全问题：同一张票被多个线程卖出
 * 会出现两个问题，
 *      第一种是多个线程售卖出了同一张票，
 *      第二种是票超售，会被卖到为负的
 */
public class TicketSaleNotSafe {
    public static void main(String[] args) {
        SellerNotSafe sellerNotSafe = new SellerNotSafe();
        for (int i = 0; i < 5; i++) {
            new Thread(sellerNotSafe, String.valueOf(i)).start();
        }
    }
}


class SellerNotSafe implements Runnable{
    private int ticketNumber = 1000;

    @Override
    public void run() {
        while (true){
            if(0 < ticketNumber){
                try {
                    // 用线程睡眠来模拟其他处理操作，以此来放大线程安全问题
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("第" + Thread.currentThread().getName() + "号线程售卖了第" + ticketNumber-- + "张票");
            } else {
                break;
            }
        }
    }
}