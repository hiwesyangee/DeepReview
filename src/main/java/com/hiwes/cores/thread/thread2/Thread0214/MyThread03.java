package com.hiwes.cores.thread.thread2.Thread0214;

/**
 * 创建了两个业务对象，在系统中出现了两个锁，所以结果也是异步的，打印:先b后a。
 * 关键字synchronized取得的锁都是对象锁，而不是把一段代码或者方法当做锁，所以:
 * 哪个线程先执行带synchronized关键字的方法，哪个线程就持有该方法所属对象的锁Lock，其他线程进入等待状态。
 *
 * 但是:这个前提是多个线程访问的是同一个对象，而访问的是多个对象，就会创建多个锁:
 * 同步单词：synchronized；
 * 异步单词：asynchronized。
 */
public class MyThread03 extends Thread {
    private HasSelfPrivateNum3 numRef;

    public MyThread03(HasSelfPrivateNum3 numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("a");
    }
}

class MyThread03_2 extends Thread {
    private HasSelfPrivateNum3 numRef;

    public MyThread03_2(HasSelfPrivateNum3 numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("b");
    }
}

class HasSelfPrivateNum3 {
    private int num = 0;

    synchronized public void addI(String username) {
        try {
            if (username.equals("a")) {
                num = 100;
                System.out.println("a set over!");
                Thread.sleep(2000);
            } else {
                num = 200;
                System.out.println("b set over!");
            }
            System.out.println(username + " num= " + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run03 {
    public static void main(String[] args) {
        HasSelfPrivateNum3 numRef1 = new HasSelfPrivateNum3();
        HasSelfPrivateNum3 numRef2 = new HasSelfPrivateNum3();

        MyThread03 AThread = new MyThread03(numRef1);
        AThread.start();
        MyThread03_2 BThread = new MyThread03_2(numRef2);
        BThread.start();

    }
}