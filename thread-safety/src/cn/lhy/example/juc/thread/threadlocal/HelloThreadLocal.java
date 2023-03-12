package cn.lhy.example.juc.thread.threadlocal;

public class HelloThreadLocal {
    public static void main(String[] args) {
        TicketSeller ticketSeller = new TicketSeller();

        System.out.println(ticketSeller.leftTicketCount.get());
        new TickerSellerThread(ticketSeller).start();
        new TickerSellerThread(ticketSeller).start();

    }
}

class TicketSeller {
    public ThreadLocal<Integer> leftTicketCount = new ThreadLocal<>();
    public TicketSeller(){
        leftTicketCount.set(20);
    }
}

class TickerSellerThread extends Thread {
    public TicketSeller seller;
    public TickerSellerThread(TicketSeller seller){
        this.seller = seller;
    }

    @Override
    public void run() {
        /*
        seller.leftTicketCount.get()会出现空指针，
        因为seller中的leftTicketCount是一个ThreadLocal变量，只在main线程中进行了初始化，在当前线程没有被初始化，get方法返回值为null， > 0操作就会产生空指针。
         */
        while (seller.leftTicketCount.get() > 0) {
            System.out.println("卖出第" + seller.leftTicketCount.get() + "张票");
            seller.leftTicketCount.set(seller.leftTicketCount.get() - 1);
        }
    }
}