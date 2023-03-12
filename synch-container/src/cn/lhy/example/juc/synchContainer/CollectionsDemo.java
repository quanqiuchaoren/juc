package cn.lhy.example.juc.synchContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 集合工具类可以将非线程安全的容器类转换为线程安全的容器类
 * Collections.synchronizedXxx(new Xxx);
 * 但是通过这种方式得到的容器，是通过悲观锁保证线程安全，
 *      实例的操作方法都是属于高开销操作，并不推荐
 */
public class CollectionsDemo {
    public static void main(String[] args) {
        SynchListThreadDemo synchListThreadDemo = new SynchListThreadDemo();
        for (int i = 0; i < 3; i++) {
            new Thread(synchListThreadDemo, String.valueOf(i)).start();
        }
    }
}

class SynchListThreadDemo implements Runnable{
    // 通过Collections工具类获取线程安全的集合
    private static List<String> synchList = Collections.synchronizedList(new ArrayList<>());
    // 对集合内容进行初始化
    static {
        synchList.add("google");
        synchList.add("apple");
        synchList.add("alibaba");
        synchList.add("tencent");
        synchList.add("huawei");
    }

    @Override
    public void run() {
        for (Iterator<String> it = synchList.iterator(); it.hasNext() ; ) {
            // 遍历的同时
            System.out.println(it.next());
            /**
             * 进行修改，这样会发生并发修改异常，即ConcurrentModificationException
             */
            synchList.add("qqkt");
        }

    }
}

