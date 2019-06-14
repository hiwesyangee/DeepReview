package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 创建读个Condition对象，实现通知部分线程。
 * 正确示例。
 */
public class MyThread06 extends Thread {
    private MyService6 service;

    public MyThread06(MyService6 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.awaitA();
    }
}

class MyThread06_2 extends Thread {
    private MyService6 service;

    public MyThread06_2(MyService6 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.awaitB();
    }
}

class MyService6 {
    private Lock lock = new ReentrantLock();

    public Condition conditionA = lock.newCondition();
    public Condition conditionB = lock.newCondition();

    public void awaitA() {
        try {
            lock.lock();
            System.out.println("begin awaitA时间为:" + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            conditionA.await();
            System.out.println("  end awaitA时间为:" + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void awaitB() {
        try {
            lock.lock();
            System.out.println("begin awaitB时间为:" + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            conditionB.await();
            System.out.println("  end awaitB时间为:" + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    public void signalAll_A() {
        try {
            lock.lock();
            System.out.println("   signalAll_A时间为:" + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            conditionA.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void signalAll_B() {
        try {
            lock.lock();
            System.out.println("   signalAll_B时间为:" + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            conditionB.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

class Run06 {
    public static void main(String[] args) throws InterruptedException {
        MyService6 service = new MyService6();
        MyThread06 a = new MyThread06(service);
        a.setName("A");
        a.start();

        MyThread06_2 b = new MyThread06_2(service);
        b.setName("B");
        b.start();

        Thread.sleep(3000);
        service.signalAll_A();
    }
}