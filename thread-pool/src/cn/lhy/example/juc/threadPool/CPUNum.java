package cn.lhy.example.juc.threadPool;

/**
 * 获取当前系统可用的CPU个数
 */
public class CPUNum {
    public static void main(String[] args) {
        // 获取可用的逻辑处理器（超线程）的数量
        int cpuNum = Runtime.getRuntime().availableProcessors();
        System.out.println("当前系统可用CPU个数 -> " + cpuNum);// 当前系统可用CPU个数 -> 12
    }
}
