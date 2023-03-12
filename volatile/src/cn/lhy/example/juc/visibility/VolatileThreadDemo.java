package cn.lhy.example.juc.visibility;

/**
 * 对线程类的变量使用了volatile关键字之后，变量的变动会立即被其他线程感知到
 */
public class VolatileThreadDemo {
    public static void main(String[] args) {
        VolatileThread volatileThread = new VolatileThread();
        new Thread(volatileThread, "使用了volatile的线程类").start();
        while(true){
            if(volatileThread.getFlag()){
                System.out.println("主线程感知到了子线程对flag的改变，执行完毕");
                break;
            }
        }
    }
}

class VolatileThread implements Runnable{
    /**
     * 将共享变量使用volatile修饰，能保证flag变量的读写都是最新的
     * volatile读之前，会先去JVM主内存里面将最新值同步到线程的工作内存
     * volatile写之后，会将线程的工作内存的共享变量刷新到主内存
     */
    private volatile boolean flag = false;
    public boolean getFlag(){
        return this.flag;
    }
    @Override
    public void run() {
        this.flag = true;
        System.out.println("子线程将flag设置为true！");
    }
}

