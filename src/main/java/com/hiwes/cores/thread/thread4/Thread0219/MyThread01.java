package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock实现同步:测试1
 * 使用ReentrantLock对象的lock()，获取锁；
 * 使用ReentrantLock对象的unlock()，释放锁；
 */
public class MyThread01 extends Thread {

    private MyService1 service;

    public MyThread01(MyService1 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.testMethod();
    }

}

class MyService1 {
    private Lock lock = new ReentrantLock();

    public void testMethod() {
        lock.lock();  // 获取锁.
        for (int i = 0; i < 5; i++) {
            System.out.println("ThreadName = " + Thread.currentThread().getName() + ("  " + (i + 1)));
        }
        lock.unlock();  // 释放锁.
    }
}


class Run01 {
    public static void main(String[] args) {
        MyService1 service = new MyService1();
        MyThread01 a1 = new MyThread01(service);
        MyThread01 a2 = new MyThread01(service);
        MyThread01 a3 = new MyThread01(service);
        MyThread01 a4 = new MyThread01(service);
        MyThread01 a5 = new MyThread01(service);
        a1.start();
        a2.start();
        a3.start();
        a4.start();
        a5.start();
    }
}