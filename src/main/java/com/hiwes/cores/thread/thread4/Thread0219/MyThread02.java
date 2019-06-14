package com.hiwes.cores.thread.thread4.Thread0219;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 调用locak.lock()代码的线程就持有了“对象监视器”，其他线程只有等待锁被释放的时候再次抢夺；
 * 效果等同于synchronized.
 */
public class MyThread02 extends Thread {
    private MyService2 service;

    public MyThread02(MyService2 service) {

        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.methodA();
    }
}

class MyThread02_2 extends Thread {

    private MyService2 service;

    public MyThread02_2(MyService2 service) {

        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.methodA();
    }
}

class MyThread02_B extends Thread {
    private MyService2 service;

    public MyThread02_B(MyService2 service) {

        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.methodB();
    }
}

class MyThread02_BB extends Thread {

    private MyService2 service;

    public MyThread02_BB(MyService2 service) {

        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.methodB();
    }
}


class MyService2 {
    private Lock lock = new ReentrantLock();

    public void methodA() {
        try {
            lock.lock();
            System.out.println("methodA begin ThreadName = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("methodA   end ThreadName = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void methodB() {
        try {
            lock.lock();
            System.out.println("methodB begin ThreadName = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("methodB   end ThreadName = " + Thread.currentThread().getName() + " time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}


class Run02 {
    public static void main(String[] args) {
        MyService2 service = new MyService2();
        MyThread02 a = new MyThread02(service);
        a.setName("A");
        a.start();
        MyThread02_2 aa = new MyThread02_2(service);
        aa.setName("AA");
        aa.start();
        MyThread02_B b = new MyThread02_B(service);
        b.setName("B");
        b.start();
        MyThread02_BB bb = new MyThread02_BB(service);
        bb.setName("BB");
        bb.start();
    }
}