package cn.lhy.example.juc.resort.non_volatile;


import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * volatile禁止指令重排序
 */
public class NonVolatileDemo {
    public static void main(String[] args) {
        NonVolatileThread nonVolatileDemo = new NonVolatileThread();
        new Thread(() -> nonVolatileDemo.setA10()).start();
        new Thread(() -> nonVolatileDemo.getSquare()).start();
    }
}

class NonVolatileThread{
    private int a = 0;
    boolean changed = false;

    public void setA10(){
        /**
         * Class.getName()返回的是全限定名称
         *      cn.lhy.example.juc.resort.non_volatile.NonVolatileThread
         * 而getSimpleName返回的只是类名
         *      NonVolatileThread
         */
        String resourcePath = this.getClass().getName().replace(".", "/").concat(".class");
        URL systemResource = ClassLoader.getSystemResource(resourcePath);
        /**
         * 获取结果如下
         * /E:/workspace/java/juc/out/production/volatile/cn/lhy/example/juc/resort/non_volatile/NonVolatileThread.class
         */
        String path = systemResource.getPath();
        File testFile = new File(new File(path).getParent() + File.separator + "test.txt");
        try(
                BufferedReader reader = new BufferedReader(new FileReader(testFile));
                ){
            String s1;
            while ((s1 = reader.readLine()) != null){
                if ("10".equals(s1)) break;
            }
            System.out.println("s1 ------------> " + s1);
            int testNum = Integer.parseInt(s1);
            this.a = testNum;
        }catch (IOException e){e.printStackTrace();}
        this.changed = true;
        System.out.println("a = 10");
    }

    public void getSquare(){
        while(!this.changed){
//            System.out.println("a not changed");
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("a*a ---> " + a*a);
    }

}