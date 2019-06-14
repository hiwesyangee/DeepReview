package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * i--与System.out.println()的异常。
 */
public class MyThread08 extends Thread {
    private int i = 5;

    @Override
    synchronized public void run() {
        System.out.println("i=" + (i--) + " threadName=" + Thread.currentThread().getName());
    }
}

class Run08 {
    public static void main(String[] args) {
        MyThread08 thread = new MyThread08();
        Thread r1 = new Thread(thread);
        Thread r2 = new Thread(thread);
        Thread r3 = new Thread(thread);
        Thread r4 = new Thread(thread);
        Thread r5 = new Thread(thread);
        r1.start();
        r2.start();
        r3.start();
        r4.start();
        r5.start();
    }
}
