package com.hiwes.cores.thread.thread3.Thread0219;

/**
 * ThreadLocal主要解决的是每个线程绑定自己的值；
 * 可以将ThreadLocal类比比喻成全局存放数据的盒子，盒子中可以存储每个线程的私有数据
 * 验证ThreadLocal中的每个线程的数据隔离性。
 * 线程A、线程B、线程Main都在往ThreadLocal中写数据，但是每个线程都能取出自己的私有数据。
 */
public class MyThread06 extends Thread {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Tools.t1.set("ThreadA " + (i + 1));
                System.out.println("ThreadA get value = " + Tools.t1.get());
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread06_2 extends Thread {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Tools.t1.set("ThreadB" + (i + 1));
                System.out.println("ThreadB get value = " + Tools.t1.get());
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Tools {
    public static ThreadLocal t1 = new ThreadLocal();
}

class Run06 {
    public static void main(String[] args) {
        try {
            MyThread06 a = new MyThread06();
            MyThread06_2 b = new MyThread06_2();
            a.start();
            b.start();
            for (int i = 0; i < 100; i++) {
                Tools.t1.set("Main" + (i + 1));
                System.out.println("Main get value = " + Tools.t1.get());
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}