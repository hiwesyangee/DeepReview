package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * 通过Thread构造方法，可以传递进一个Thread或Runnable。
 */
public class MyThread04 implements Runnable {

    @Override
    public void run() {
        System.out.println("运行中!");
    }
}

class Run04{
    public static void main(String[] args) {
        Runnable runnable = new MyThread04();
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println("运行结束!");
    }
}
