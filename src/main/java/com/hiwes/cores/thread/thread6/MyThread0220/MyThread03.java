package com.hiwes.cores.thread.thread6.MyThread0220;

/**
 * 延时加载/懒汉模式的解决方案。
 * 多个线程同时进行getInstance()方法，那么只需要对getInstance()进行加锁即可。
 * getInstance()方法进行了同步，效率很低！
 */
public class MyThread03 extends Thread {

    @Override
    public void run() {
        System.out.println(MyObject03.getInstance().hashCode());
    }
}

class MyObject03 {
    private static MyObject03 myObject;

    private MyObject03() {
    }

    // 设置同步方法效率很低————整个方法都上锁了
    synchronized public static MyObject03 getInstance() {
        try {
            if (myObject != null) {
            } else {
                Thread.sleep(3000);
                myObject = new MyObject03();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return myObject;
    }
}

class Run03 {
    public static void main(String[] args) {
        MyThread03 t1 = new MyThread03();
        MyThread03 t2 = new MyThread03();
        MyThread03 t3 = new MyThread03();
        t1.start();
        t2.start();
        t3.start();
    }
}