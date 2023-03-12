package cn.lhy.example.juc.thread.threadlocal;

/**
 * Created by Enzo Cotter on 2020/6/10.
 */
public class InitialValueDemo {

    public static void main(String[] args) {
        // 分配给每个线程20张票
        ThreadLocalThread threadLocalThread = new ThreadLocalThread(20);
        for (int i = 0; i < 5; i++) {
            new Thread(threadLocalThread, String.valueOf(i)).start();
        }
    }
}

class ThreadLocalThread implements Runnable{
    // 线程本地私有变量
    private static ThreadLocal<Integer> ticketNumber = new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return 20;
        }
    };

    public ThreadLocalThread(int _ticketNumber){
        ticketNumber.set(_ticketNumber);
        System.out.println("this.ticketNumber.get()->" + this.ticketNumber.get());
    }

    @Override
    public void run() {
        while (true){
            if(0 < ticketNumber.get()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "号线程卖出了属于它的第" + ticketNumber.get() + "张票");
                ticketNumber.set(ticketNumber.get()-1);
            } else {
                System.out.println(Thread.currentThread() + "号线程售票完毕");
                break;
            }
        }

    }
}
