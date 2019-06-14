package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Condition实现等待/通知.
 * 错误示例.
 */
public class MyThread03 extends Thread {
    private MyService3 service;

    public MyThread03(MyService3 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.await();
    }
}


class MyService3 {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void await() {
        try {
            lock.lock();
            System.out.println("A");
            condition.await();  // 调用此方法，进入了waiting状态.
            System.out.println("B");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("锁被释放了.");
        }
    }
}


class Run03 {
    public static void main(String[] args) {
        MyService3 service = new MyService3();
        MyThread03 a = new MyThread03(service);
        a.start();
    }
}

/**
 * 会报错:java.lang.IllegalMonitorStateException
 * 解决办法:
 * 在condition.await()方法调用之前调用lock.lock()获取同步监视器.
 */