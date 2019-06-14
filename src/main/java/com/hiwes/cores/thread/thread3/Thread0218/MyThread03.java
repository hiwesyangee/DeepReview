package com.hiwes.cores.thread.thread3.Thread0218;

public class MyThread03 extends Thread {
    private Object lock;

    public MyThread03(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                System.out.println("开始      wait time = " + System.currentTimeMillis());
                lock.wait();
                System.out.println("结束      wait time = " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread03_2 extends Thread {
    private Object lock;

    public MyThread03_2(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            System.out.println("开始      notify time = " + System.currentTimeMillis());
            lock.notify();
            System.out.println("结束      notify time = " + System.currentTimeMillis());
        }
    }
}

class Run03 {
    public static void main(String[] args) {
        try {
            Object lock = new Object();
            MyThread03 a = new MyThread03(lock);
            a.start();
            Thread.sleep(3000);
            MyThread03_2 b = new MyThread03_2(lock);
            b.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}