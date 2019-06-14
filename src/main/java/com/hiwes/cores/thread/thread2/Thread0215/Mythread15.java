package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 验证:对象锁和class锁，不是同一个锁。
 * 结果异步的原因:
 * 持有不同的锁，一个是对象锁，一个是class锁，其中class锁可以对类的所有对象实例起作用。
 */
public class Mythread15 extends Thread {

    private Service15 service;

    public Mythread15(Service15 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.printA();
    }
}

class MyThread15_2 extends Thread {

    private Service15 service;

    public MyThread15_2(Service15 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.printB();
    }
}


class MyThread15_3 extends Thread {

    private Service15 service;

    public MyThread15_3(Service15 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.printC();
    }
}

class Service15 {
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

    synchronized public void printC() {
        System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " into  printC().");
        System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " leave printC().");

    }
}

class Run15 {
    public static void main(String[] args) {
        Service15 service = new Service15();
        Mythread15 a = new Mythread15(service);
        a.setName("A");
        a.start();
        MyThread15_2 b = new MyThread15_2(service);
        b.setName("B");
        b.start();
        MyThread15_3 c = new MyThread15_3(service);
        b.setName("C");
        c.start();
    }
}

