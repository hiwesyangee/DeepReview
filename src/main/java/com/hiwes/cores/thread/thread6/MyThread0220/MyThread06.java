package com.hiwes.cores.thread.thread6.MyThread0220;

/**
 * 使用DCL双检查锁机制:
 * Double-check locking
 */
public class MyThread06 extends Thread {

    @Override
    public void run() {
        System.out.println(MyObject06.getInstance().hashCode());
    }
}

class MyObject06 {
    private volatile static MyObject06 myObject;  // 锁1

    private MyObject06() {
    }

    public static MyObject06 getInstance() {
        try {
            if (myObject != null) {
            } else {
                Thread.sleep(3000);
                synchronized (MyObject06.class) {  // 锁2
                    if (myObject == null) {
                        myObject = new MyObject06();
                    }
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return myObject;
    }
}


class Run06 {
    public static void main(String[] args) {
        MyThread06 t1 = new MyThread06();
        MyThread06 t2 = new MyThread06();
        MyThread06 t3 = new MyThread06();
        t1.start();
        t2.start();
        t3.start();
    }
}
