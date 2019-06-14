package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * 使用isAlive，判断线程是否处于活动状态--------已启动，且尚未终止。
 */
public class MyThread10 extends Thread {
    @Override
    public void run() {
        System.out.println("run= " + this.isAlive());
    }
}

class Run10 {
    public static void main(String[] args) throws InterruptedException {
        MyThread10 thread = new MyThread10();
        System.out.println("begin== " + thread.isAlive());
        thread.start();
//        thread.run();
        Thread.sleep(1000);
        System.out.println("end== " + thread.isAlive());
    }
}
