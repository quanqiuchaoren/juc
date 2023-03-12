package cn.lhy.example.juc.singleton;

/**
 * 使用静态内部类来实践单例模式
 */
public class StaticInnerClassSingletonTest {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> System.out.println(StaticInnerClassSingleton.getInstance())).start();
        }
    }
}

class StaticInnerClassSingleton{
    /**
     * 使用静态内部类
     * 静态内部类的优点：外部类加载时，并不需要立即加载内部类，故而不占内存
     * 只有当getInstance()方法第一次被调用时，才会初始化INSTANCE，并且虚拟机会保证线程安全
     * 静态内部类实现单例有一个缺点，创建单例的时候，不能传递参数
     */
    private static class SingletonHolder{
        private static StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }
    public static StaticInnerClassSingleton getInstance(){
        return SingletonHolder.INSTANCE;
    }
}