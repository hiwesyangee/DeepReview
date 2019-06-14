package com.hiwes.cores.thread.thread6.MyThread0220;

/**
 * 使用同步代码块的方式，进行上锁。
 * getInstance()方法内部依然是进行了同步的，效率依然很低！
 */
public class MyThread04 extends Thread {

    @Override
    public void run() {
        System.out.println(MyObject04.getInstance().hashCode());
    }
}

class MyObject04 {
    private static MyObject04 myObject;

    private MyObject04() {
    }

    public static MyObject04 getInstance() {
        try {
            synchronized (MyObject04.class) {
                if (myObject != null) {
                } else {
                    Thread.sleep(3000);
                    myObject = new MyObject04();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return myObject;
    }
}


class Run04{
    public static void main(String[] args) {
        MyThread04 t1 = new MyThread04();
        MyThread04 t2 = new MyThread04();
        MyThread04 t3 = new MyThread04();
        t1.start();
        t2.start();
        t3.start();
    }
}