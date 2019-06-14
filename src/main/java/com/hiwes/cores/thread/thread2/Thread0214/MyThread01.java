package com.hiwes.cores.thread.thread2.Thread0214;

/**
 * 方法内部的变量，是线程安全的，不会出现非线程安全。
 */
public class MyThread01 extends Thread {
    private HasSelfPrivateNum numRef;

    public MyThread01(HasSelfPrivateNum numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("a");
    }

}


class MyThread01_2 extends Thread {
    private HasSelfPrivateNum numRef;

    public MyThread01_2(HasSelfPrivateNum numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("b");
    }

}

class Run01 {
    public static void main(String[] args) {
        HasSelfPrivateNum numRef = new HasSelfPrivateNum();
        MyThread01 AThread = new MyThread01(numRef);
        AThread.start();
        MyThread01_2 BThread = new MyThread01_2(numRef);
        BThread.start();
    }
}

class HasSelfPrivateNum {
    public void addI(String username) {
        try {
            int num;
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