package com.hiwes.cores.thread.thread3.Thread0218;

public class MyThread02 {
}

/**
 * 出现异常的原因:没有“对象监视器”，也就是没有同步加锁。
 * 使用wait()的示例。
 */
class Run02Test1 {
    public static void main(String[] args) {
        try {
            String newString = new String("");
            newString.wait();   // Object类下面的方法。
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run02Test2{
    public static void main(String[] args) {
        try{
            String lock = new String();
            System.out.println("syn上面.");
            synchronized (lock){
                System.out.println("syn第一行.");
                lock.wait();  // wait()下面的代码就不执行了。
                System.out.println("wait下面的代码.");
            }
            System.out.println("syn下面.");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}