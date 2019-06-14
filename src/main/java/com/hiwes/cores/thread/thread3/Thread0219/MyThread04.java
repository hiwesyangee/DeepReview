package com.hiwes.cores.thread.thread3.Thread0219;

/**
 * join(long)和sleep(long)的区别:
 * join(long)内部使用的wait(long)方法来实现的，所以具有释放锁的特点;
 * sleep(long)则没有释放锁的特点.
 */
public class MyThread04 extends Thread {

    private MyThread04_2 b;

    public MyThread04(MyThread04_2 b) {
        super();
        this.b = b;
    }

    @Override
    public void run() {
        try {
            synchronized (b) {
                b.start();
//                Thread.sleep(6000);  // 不释放锁.
                b.join();  // 释放了锁.
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    String newString = new String();
                    Math.random();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


class MyThread04_2 extends Thread {
    @Override
    public void run() {
        try {
            System.out.println("  b run begin timer = " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("  b run   end timer = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public void bService() {
        System.out.println("打印了bService timer = " + System.currentTimeMillis());
    }
}


class MyThread04_3 extends Thread {
    private MyThread04_2 b;

    public MyThread04_3(MyThread04_2 b) {
        super();
        this.b = b;
    }

    @Override
    public void run() {
        b.bService();
    }
}


class Run04 {
    public static void main(String[] args) {
        try {
            MyThread04_2 b = new MyThread04_2();
            MyThread04 a = new MyThread04(b);
            a.start();
            Thread.sleep(1000);
            MyThread04_3 c = new MyThread04_3(b);
            c.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}