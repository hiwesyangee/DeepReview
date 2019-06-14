package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Condition创建多个对象，实现通知部分对象。
 * 错误示例。
 */
public class MyThread05 extends Thread {

    private MyService5 service;

    public MyThread05(MyService5 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.awaitA();
    }
}

class MyThread05_2 extends Thread {
    private MyService5 service;

    public MyThread05_2(MyService5 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.awaitB();
    }
}

class MyService5 {
    private Lock lock = new ReentrantLock();

    public Condition condition = lock.newCondition();

    public void awaitA() {
        try {
            lock.lock();
            System.out.println("begin awaitA时间为:" + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            condition.await();
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
            condition.await();
            System.out.println("  end awaitB时间为:" + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void singalAll() {
        try {
            lock.lock();
            System.out.println("  signalAll时间为:" + System.currentTimeMillis() + " ThreadName = " + Thread.currentThread().getName());
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

class Run05 {
    public static void main(String[] args) throws InterruptedException {
        MyService5 service = new MyService5();
        MyThread05 a = new MyThread05(service);
        a.setName("A");
        a.start();
        MyThread05_2 b = new MyThread05_2(service);
        b.setName("B");
        b.start();
        Thread.sleep(3000);
        service.singalAll();
    }
}
