package cn.lhy.example.juc.visibility;

/**
 * 使用同步代码块也可以刷新共享变量的值
 *      但是这样同步数据的方式，比volatile关键字的开销更高，因为需要加锁，而加锁是重量级操作
 */
public class SynchronizedThreadDemo {
    public static void main(String[] args) {
        Object lock = new Object();
        SynchronizedThread nonVolatileThread = new SynchronizedThread();
        new Thread(nonVolatileThread).start();
        while(true){
            /**
             * 在同步代码块内，
             *      在加锁前，会强制从主存中取最新的的共享变量的值（同步代码块内的共享变量）
             *      加解锁前，会强制将同步代码块内的共享变量刷新到主内存中
             */
            synchronized (lock){
                if(nonVolatileThread.getFlag()){
                    System.out.println("主线程感知到了子线程对flag的改变，执行完毕");
                    break;
                }
            }

        }
    }
}

class SynchronizedThread implements Runnable{
    private boolean flag = false;
    public boolean getFlag(){
        return this.flag;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.flag = true;
        System.out.println("子线程将flag设置为" + getFlag() + "！");
    }
}