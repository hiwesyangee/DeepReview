package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 加synchronized到static方法上，从本质上来说，是对所在类进行了加锁，而不再是对象。
 */
public class MyThread14 extends Thread {

    @Override
    public void run() {
        Service14.printA();
    }
}

class MyThread14_2 extends Thread {

    @Override
    public void run() {
        Service14.printB();
    }
}

class Service14 {
    synchronized public static void printA() {
        try {
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " into  printA().");
            Thread.sleep(3000);
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " leave printA().");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public static void printB() {
        System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " into  printB().");
        System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " leave printB().");
    }
}

class Run14 {
    public static void main(String[] args) {
        MyThread14 a = new MyThread14();
        a.setName("A");
        a.start();
        MyThread14_2 b = new MyThread14_2();
        b.setName("B");
        b.start();
    }
}