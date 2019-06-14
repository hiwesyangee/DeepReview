package com.hiwes.cores.thread.thread6.MyThread0220;

/**
 * 针对懒汉模式的更进一步性能提升。但是不能解决单例的需求，出来的对象是多个。
 */
public class MyThread05 extends Thread {

    @Override
    public void run() {
        System.out.println(MyObject05.getInstance().hashCode());
    }
}

class MyObject05 {
    private static MyObject05 myObject;

    private MyObject05() {
    }

    public static MyObject05 getInstance() {
        try {

            if (myObject != null) {
            } else {
                Thread.sleep(3000);
                synchronized (MyObject04.class) {  // 部分代码上锁，但是还是有非线程安全问题。
                    myObject = new MyObject05();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return myObject;
    }
}


class Run05 {
    public static void main(String[] args) {
        MyThread05 t1 = new MyThread05();
        MyThread05 t2 = new MyThread05();
        MyThread05 t3 = new MyThread05();
        t1.start();
        t2.start();
        t3.start();
    }
}