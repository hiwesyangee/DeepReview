package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 使用String进作为synchronized对象锁的对象，会因为String常量池造成持有的锁是同一个
 *
 * 同步synchronized代码块都不适用String作为锁对象，而改用其他，
 * 比如new Object()实例化一个Object对象，但它并不放入缓存中。
 * 见MyThread18.java。
 */
public class Mythread17 extends Thread {

    private Service17 service;

    public Mythread17(Service17 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.print("AA");
    }
}

class Mythread17_2 extends Thread {
    private Service17 service;

    public Mythread17_2(Service17 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.print("AA");
    }
}

class Service17 {
    public static void print(String stringParam) {
        try {
            synchronized (stringParam) {
                while (true) {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run17 {
    public static void main(String[] args) {
        Service17 service = new Service17();
        Mythread17 a = new Mythread17(service);
        a.setName("A");
        a.start();

        Mythread17_2 b = new Mythread17_2(service);
        b.setName("B");
        b.start();
    }
}