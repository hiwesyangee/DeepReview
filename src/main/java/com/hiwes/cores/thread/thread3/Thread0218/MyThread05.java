package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * 使用wait(),会自动释放对象锁，从而在调用同步方法的时候，实现线程同步。
 */
public class MyThread05 extends Thread {
    private Object lock;

    public MyThread05(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service5 service = new Service5();
        service.testMethod(lock);
    }
}

class MyThread05_2 extends Thread {
    private Object lock;

    public MyThread05_2(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service5 service = new Service5();
        service.testMethod(lock);
    }
}

class Service5 {
    public void testMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin wait().");
                // lock.wait();
                Thread.sleep(4000);    // 使用sleep()，达到同步的效果。
                System.out.println("  end wait().");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run05 {
    public static void main(String[] args) {
        Object lock = new Object();
        MyThread05 a = new MyThread05(lock);
        a.start();
        MyThread05_2 b = new MyThread05_2(lock);
        b.start();
    }
}