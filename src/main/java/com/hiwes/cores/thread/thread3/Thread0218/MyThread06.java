package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * notify()方法执行后，不会立即释放锁。
 * 同时wait()被唤醒之后，会跟其他的线程进行资源抢夺。
 */
public class MyThread06 extends Thread {
    private Object lock;

    public MyThread06(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service6 service = new Service6();
        service.testMethod(lock);
    }
}


class MyThread06_2 extends Thread {
    private Object lock;

    public MyThread06_2(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service6 service = new Service6();
        service.synNotifyMethod(lock);
    }
}

class MyThread06_3 extends Thread {
    private Object lock;

    public MyThread06_3(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service6 service = new Service6();
        service.synNotifyMethod(lock);
    }
}

class Service6 {
    public void testMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin wait() ThreadName = " + Thread.currentThread().getName());
                lock.wait();
                System.out.println("  end wait() ThreadName = " + Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void synNotifyMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin notify() ThreadName = " + Thread.currentThread().getName());
                lock.notify();  // 不会立即释放锁
                Thread.sleep(5000);
                System.out.println("  end notify() ThreadName = " + Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run06 {
    public static void main(String[] args) {
        Object lock = new Object();
        MyThread06 a = new MyThread06(lock);
        a.start();
        MyThread06_2 b = new MyThread06_2(lock);
        b.start();
        MyThread06_3 c = new MyThread06_3(lock);
        c.start();
    }
}