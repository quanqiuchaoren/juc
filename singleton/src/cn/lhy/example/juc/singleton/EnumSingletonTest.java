package cn.lhy.example.juc.singleton;

/**
 * 可以用枚举实现单例
 * 因为枚举代码编译之后，会被编译成
 * public static final
 */
public class EnumSingletonTest {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> System.out.println(EnumSingleton.INSTANCE.getEnumClass())).start();
        }
    }
}

enum EnumSingleton {
    INSTANCE(new EnumClass());

    EnumSingleton(EnumClass enumClass) {
        this.enumClass = enumClass;
    }

    private EnumClass enumClass;

    public EnumClass getEnumClass() {
        return enumClass;
    }
}

class EnumClass {

}