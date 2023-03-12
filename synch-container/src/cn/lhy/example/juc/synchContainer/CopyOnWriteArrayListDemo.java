package cn.lhy.example.juc.synchContainer;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * juc包为多线程环境下的开发提供了很多线程安全的集合工具类，
 * 这些集合工具类都是使用的volatile和CAS算法来保证线程安全，属于乐观锁技术
 *      比通过Collections.synchronizedXxx(new Xxx)得到的线程安全的集合类性能更优异
 * 比如，ConcurrentHashMap、ConcurrentSkipListMap、
 *      CopyOnWriteArrayList、CopyOnWriteArraySet
 *      这些类都是属于juc包，对应关系如下
 *      ConcurrentHashMap -> HashMap
 *      ConcurrentSkipListMap -> TreeMap
 *      CopyOnWriteArrayList -> ArrayList
 *      CopyOnWriteArraySet -> ArraySet
 */
public class CopyOnWriteArrayListDemo {
    public static void main(String[] args) {
        CopyOnWriteArrayListThread copyOnWriteArrayListThread = new CopyOnWriteArrayListThread();
        for (int i = 0; i < 1; i++) {
            new Thread(copyOnWriteArrayListThread, String.valueOf(i)).start();
        }
    }
}

class CopyOnWriteArrayListThread implements Runnable{
    // 新建一个juc包里面的线程安全的列表
    private static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
    // 列表初始化
    static {
        list.add("google");
        list.add("apple");
        list.add("alibaba");
    }

    @Override
    public void run() {
        for (Iterator<String> it = list.iterator(); it.hasNext();) {
            // 在遍历的同时
            System.out.print(Thread.currentThread().getName()+"号线程 -> ");
            System.out.println(it.next());
            /**
             * 修改列表，
             * 由于是juc包的列表集合，所以不会报并发修改异常
             * 这里有个疑问，每次遍历，都会往列表的最后追加一个节点，
             * 但是，程序会在有限次的输出后就停止了？？？？？？？？
             * 为什么？？？？？？？？？？
             */
            list.add("qqkt");
        }
    }
}