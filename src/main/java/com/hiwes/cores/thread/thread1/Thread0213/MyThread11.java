package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * sleep()：是正在运行的线程进入休眠状态。
 */
public class MyThread11 extends Thread {

    @Override
    public void run() {
        try {
            System.out.println("run threadName=" + this.currentThread().getName() + " begin");
            Thread.sleep(2000);
            System.out.println("run threadName=" + this.currentThread().getName() + " end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Run11{
    public static void main(String[] args) {
        MyThread11 thread = new MyThread11();
        System.out.println("begin ="+System.currentTimeMillis());
        thread.run();
        System.out.println("end   ="+System.currentTimeMillis());
    }
}