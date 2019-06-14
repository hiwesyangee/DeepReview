package com.hiwes.cores.thread.thread2.Thread0218;

/**
 * 添加同步代码块，保证在同一时刻，只有一个线程可移植性某一方法或者某一代码块，包含两个特性:互斥性和可见性。
 * 同步synchronized不仅可以解决一个线程看到对象处于不一致的状态，还可以保证进入方法或者同步代码块的每个线程，
 * 都看到由同一个锁保护之前所有的修改效果。
 */
public class MyThread12 extends Thread {
    private Service service;

    public MyThread12(Service service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.runMethod();
    }
}

class MyThread12_2 extends Thread {
    private Service service;

    public MyThread12_2(Service service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.stopMethod();
    }
}


class Service {
    private boolean isContinueRun = true;

    public void runMethod() {
        String anyThing = new String();
        while (isContinueRun == true) {
            synchronized (anyThing) { // 添加同步代码块，保证在同一时刻，只有一个线程可移植性某一方法或者某一代码块，包含两个特性:互斥性和可见性。

            }
        }
        System.out.println("停下来了!");
    }

    public void stopMethod() {
        isContinueRun = false;
    }
}

class Run12 {
    public static void main(String[] args) {
        try {
            Service service = new Service();
            MyThread12 a = new MyThread12(service);
            a.start();
            Thread.sleep(1000);
            MyThread12_2 b = new MyThread12_2(service);
            b.start();
            System.out.println("已经发起停止的命令了.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}