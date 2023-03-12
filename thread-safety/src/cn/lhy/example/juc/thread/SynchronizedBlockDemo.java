package cn.lhy.example.juc.thread;

/**
 * 使用同步代码块来解决线程安全问题
 * 使用加锁的方式，可以保证临界区内的代码的操作是原子性的，不可分割的
 * 并且在加锁前，会将变量从主内存里面重新加载到线程的工作内存中
 */
public class SynchronizedBlockDemo {
    public static void main(String[] args) {
        SynchronizedBlockThread synchronizedBlockThread = new SynchronizedBlockThread();
        for (int i = 0; i < 5; i++) {
            new Thread(synchronizedBlockThread, String.valueOf(i)).start();
        }
    }

}

class SynchronizedBlockThread implements Runnable{
    // 总票数
    private int ticketNumber = 100;
    /**
     * 新建给一个对象来当做同步锁，以此来解决线程安全问题，即一张票被多次售卖以及超售到负数这两问题
     * 同步锁可以是任意对象，但是最好不能为null，
     */
    private Object synchronizedLock = new Object();
    @Override
    public void run() {
        while(true){
            // 这里以自己创建的对象来当做同步锁，其实也可以以当前对象来当做同步锁，即this
            synchronized (synchronizedLock){
                if(0 < ticketNumber) {
                    System.out.println("第" + Thread.currentThread().getName() + "号线程售卖出了第" + ticketNumber-- + "张票");
                } else {
                    // 在同步代码块中执行break，会让当前线程释放同步锁，与重入锁有区别
                    break;
                }
            }
        }
    }
}



