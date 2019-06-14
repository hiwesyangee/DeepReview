package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现生产者/消费者模式:一对一交替打印.
 */
public class MyThread07 extends Thread {
    private MyService7 service;

    public MyThread07(MyService7 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            service.set();
        }
    }

}


class MyThread07_2 extends Thread {
    private MyService7 service;

    public MyThread07_2(MyService7 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            service.get();
        }
    }

}


class MyService7 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean hasValue = false;

    public void set() {
        try {
            lock.lock();
            while (hasValue == true) {
                condition.await();
            }
            System.out.println("打印 ※ ");
            hasValue = true;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void get() {
        try {
            lock.lock();
            while (hasValue == false) {
                condition.await();
            }

            System.out.println("打印 ✨ ");
            hasValue = false;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}


class Run07 {
    public static void main(String[] args) {
        MyService7 service = new MyService7();
        MyThread07 a = new MyThread07(service);
        a.start();
        MyThread07_2 b = new MyThread07_2(service);
        b.start();
    }
}
