package com.hiwes.cores.thread.thread2.Thread0215;

public class MyThread16 extends Thread {

    private Service16 service;

    public MyThread16(Service16 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.printA();
    }
}


class MyThread16_2 extends Thread {

    private Service16 service;

    public MyThread16_2(Service16 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.printB();
    }
}


class Service16 {
    public static void printA() {
        synchronized (Service16.class) {
            try {
                System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " into  printA().");
                Thread.sleep(3000);
                System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " leave printA().");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printB() {
        synchronized (Service16.class) {
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " into  printB().");
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " at time = " + System.currentTimeMillis() + " leave printB().");

        }
    }
}

class Run16 {
    public static void main(String[] args) {
        Service16 service1 = new Service16();
        Service16 service2 = new Service16();

        MyThread16 a = new MyThread16(service1);
        a.setName("A");
        a.start();

        MyThread16_2 b = new MyThread16_2(service2);
        b.setName("B");
        b.start();
    }
}