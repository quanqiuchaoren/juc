package read_write_lock;

import java.util.concurrent.TimeUnit;

/**
 * 测试在单线程写，多线程读的场景下，是否要加读写锁。
 * 根据这个博客，是需要加锁的，但是这个博客讲的场景中，我认为大对象的场景下是对的：https://blog.csdn.net/zhouwenjun0820/article/details/107392043
 * 对于int、long、String型的对象，只是赋值，在cpu中应该是一个原语操作，应该是可以保证原子性的，比如我在RHEV收集器中取消锁的操作。
 */
public class NeedLockTest {
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (true) {
                for (int i = 0; i < 5; i++) {
                    MyToken.tokenString = "" + i;
                }
            }
        }).start();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
                    String tokenString = MyToken.tokenString;
                    if (Integer.parseInt(tokenString) >= 5) {
                        // 运行了一个晚上+一个上午，这行代码没有输出过，且程序没有退出，一般来讲，这个类上面的注释应该是对的了
                        System.out.println("读取到错误数据了, tokenString = " + tokenString);
                        System.exit(-1);
                    } else {
//                        System.out.println(666);
                    }
                }
            }).start();
        }

        TimeUnit.MINUTES.sleep(5);
    }
}

class MyToken {
    static volatile String tokenString = "1";
}
