package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 正确使用Condition实现等待/通知.
 *
 * 1）Object类中的wait()========>Condition类中的await()
 * 2）Object类中的wait(long)========>Condition类中的await(long)
 * 3）Object类中的notify()========>Condition类中的signal()
 * 4）Object类中的notifyAll()========>Condition类中的signalAll()
 */
public class MyThread04 extends Thread {

    private MyService4 service;

    public MyThread04(MyService4 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.await();
    }
}


class MyService4 {
    private Lock lock = new ReentrantLock();

    public Condition condition = lock.newCondition();

    public void await() {
        try {
            lock.lock();
            System.out.println(" await时间为:" + System.currentTimeMillis());
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        try {
            lock.lock();
            System.out.println("signal时间为:" + System.currentTimeMillis());
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}

class Run04 {
    public static void main(String[] args) throws InterruptedException {
        MyService4 service = new MyService4();
        MyThread04 a = new MyThread04(service);
        a.start();
        Thread.sleep(3000);
        service.signal();
    }
}