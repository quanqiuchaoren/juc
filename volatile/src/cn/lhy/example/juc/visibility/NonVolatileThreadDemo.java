package cn.lhy.example.juc.visibility;

/**
 * 没有volatile关键字的时候，一个线程对共享变量的编辑，不能及时的被其他线程感知到
 * 导致主线程一直不能退出死循环，程序一直不能结束运行
 */
public class NonVolatileThreadDemo {
    public static void main(String[] args) {
        NonVolatileThread nonVolatileThread = new NonVolatileThread();
        new Thread(nonVolatileThread, "没有使用volatile的线程类").start();
        while(true){
            /**
             * 当while循环中，除了判断语句，还有其他语句时，比如输出到控制台、睡眠等会让线程阻塞的语句
             *      就会导致循环之间有间隙，间隙之间，主线程就会去同步共享变量的最新值
             */
//            System.out.println(nonVolatileThread.getFlag());
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            /**
             * 当while循环里面只有一个判断语句的时候，主线程可能一直不会执行完毕，
             * 因为while循环和判断都是很底层的操作，执行速度快，所以线程没有空隙去同步要用到的同享变量
             */
            if(nonVolatileThread.getFlag()){
                System.out.println("主线程感知到了子线程对flag的改变，执行完毕");
                break;
            }
        }
    }
}

class NonVolatileThread implements Runnable{
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