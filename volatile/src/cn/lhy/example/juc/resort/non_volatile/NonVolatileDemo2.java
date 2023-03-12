package cn.lhy.example.juc.resort.non_volatile;



/**
 * volatile禁止指令重排序
 */
public class NonVolatileDemo2 {
    private volatile int i = 0;
    private volatile int j = 0;

    public static void main(String[] args) {
        NonVolatileDemo2 nonVolatileDemo2 = new NonVolatileDemo2();
        Runnable thread1  = () -> {
            while(true)
                nonVolatileDemo2.selfPlus();
        };

        Runnable thread2 = () -> {
            while(true) nonVolatileDemo2.print();
        };
        new Thread(thread1).start();
        new Thread(thread2).start();
    }

    public synchronized void selfPlus() {
        i++;
        j++;
    }

    public void print(){
        if(i < j)
            System.out.println("i= "+i+" ; j= "+j);
    }
}

