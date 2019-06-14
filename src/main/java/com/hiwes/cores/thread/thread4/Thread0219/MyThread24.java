package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁ReentrantReadWriteLock类。
 * 写写互斥。
 * lock.writeLock()写锁，同一时间只能有一个线程执行.lock()后面的代码。
 */
public class MyThread24 extends Thread {

    private Service24 service;

    public MyThread24(Service24 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.read();
    }
}

class MyThread24_2 extends Thread {
    private Service24 service;

    public MyThread24_2(Service24 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.write();
    }
}

class Service24 {
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

    public void write() {
        try {
            try {
                lock.writeLock().lock();
                System.out.println("获得写锁: " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
                Thread.sleep(10000);
            } finally {
                lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run24 {
    public static void main(String[] args) throws InterruptedException {
        Service24 service = new Service24();
        MyThread24 a = new MyThread24(service);
        a.setName("A");
        a.start();
        Thread.sleep(1000);
        MyThread24_2 b = new MyThread24_2(service);
        b.setName("B");
        b.start();
    }
}
