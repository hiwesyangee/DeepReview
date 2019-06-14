package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁ReentrantReadWriteLock类。
 * 读读共享。
 * lock.readLock()读锁，允许多个线程同时执行lock()方法后的代码。
 */
public class MyThread23 extends Thread {

    private Service23 service;

    public MyThread23(Service23 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.read();
    }
}

class MyThread23_2 extends Thread {
    private Service23 service;

    public MyThread23_2(Service23 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.read();
    }
}

class Service23 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void read() {
        try {
            try {
                lock.readLock().lock();
                System.out.println("获得读锁:" + Thread.currentThread().getName() + " " + System.currentTimeMillis());
                Thread.sleep(10000);
            } finally {
                lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run23 {
    public static void main(String[] args) {
        Service23 service = new Service23();
        MyThread23 a = new MyThread23(service);
        a.setName("A");
        MyThread23_2 b = new MyThread23_2(service);
        b.setName("B");
        a.start();
        b.start();
    }
}


