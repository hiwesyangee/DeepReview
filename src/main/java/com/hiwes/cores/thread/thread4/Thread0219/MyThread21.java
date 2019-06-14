package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.Calendar;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用boolean awaitUntil(Date deadline).
 * 在等待时间之内可以被其它线程唤醒，等待时间一过该线程会自动唤醒，和别的线程争抢锁资源，从而实现绝对定时条件等待。
 */
public class MyThread21 extends Thread {
    private Service21 service;

    public MyThread21(Service21 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.waitMethod();
    }
}

class MyThread21_2 extends Thread {
    private Service21 service;

    public MyThread21_2(Service21 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.notifyMethod();
    }
}

class Service21 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void waitMethod() {
        try {
            Calendar calendarRef = Calendar.getInstance();
            calendarRef.add(Calendar.SECOND, 10);
            lock.lock();
            System.out.println("wait begin time = " + System.currentTimeMillis());
            condition.awaitUntil(calendarRef.getTime());
            System.out.println("wait   end time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void notifyMethod() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, 10);
            lock.lock();
            System.out.println("notify begin time = " + System.currentTimeMillis());
            condition.notifyAll();
            System.out.println("notify   end time = " + System.currentTimeMillis());
        } finally {
            lock.lock();
        }
    }
}

class Run21 {
    public static void main(String[] args) {
        Service21 service = new Service21();
        MyThread21 thread = new MyThread21(service);
        thread.start();
    }
}

class Run21_2 {
    public static void main(String[] args) throws InterruptedException {
        Service21 service = new Service21();
        MyThread21 a = new MyThread21(service);
        a.start();
        Thread.sleep(2000);
        MyThread21_2 b = new MyThread21_2(service);
        b.start();
    }
}

