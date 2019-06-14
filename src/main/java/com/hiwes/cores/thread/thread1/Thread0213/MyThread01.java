package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * start()和run()的区别
 */
public class MyThread01 extends Thread {

    @Override
    public void run() {
        super.run();
        System.out.println("MyThread.");
    }
}


class Run01 {
    public static void main(String[] args) {
        MyThread01 thread = new MyThread01();
        thread.start();
        // thread.run();
        System.out.println("运行结束。");
    }
}