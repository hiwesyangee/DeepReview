package com.hiwes.cores.thread.thread2.Thread0214;

/**
 * 使用synchronized关键字，排除多个线程访问一个没有同步的方法的时候，造成的非线程安全问题。
 */
public class MyThread02 extends Thread {

    private HasSelfPrivateNum2 numRef;

    public MyThread02(HasSelfPrivateNum2 numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("a");
    }
}

class MyThread02_2 extends Thread {

    private HasSelfPrivateNum2 numRef;

    public MyThread02_2(HasSelfPrivateNum2 numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("b");
    }
}

class Run02 {
    public static void main(String[] args) {
        HasSelfPrivateNum2 numRef = new HasSelfPrivateNum2();
        MyThread02 AThread = new MyThread02(numRef);
        AThread.start();
        MyThread02_2 BThread = new MyThread02_2(numRef);
        BThread.start();
    }
}

class HasSelfPrivateNum2 {
    private int num = 0;

    synchronized public void addI(String username) { // 添加synchronized关键字，进行线程同步，消除非线程安全。
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