package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * 当interrupt()遇到wait()方法:
 * 当线程处于wait状态时，调用线程对象的interrupt()方法会出现InterruptedException异常。
 */
public class MyThread07 extends Thread {
    private Object lock;

    public MyThread07(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service7 service = new Service7();
        service.testMethod(lock);
    }
}

class Service7 {
    public void testMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin wait.");
                lock.wait();
                System.out.println("  end wait.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("出现异常了，疑问诶呈wait状态的线程被interrupt了！");
        }
    }
}

class Run07 {
    public static void main(String[] args) {
        try {
            Object lock = new Object();
            MyThread07 a = new MyThread07(lock);
            a.start();
            Thread.sleep(5000);
            a.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}