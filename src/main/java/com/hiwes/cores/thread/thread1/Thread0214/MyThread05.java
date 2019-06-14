package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 停止线程————sleep中被停止。
 * 先sleep，再中断。
 */
public class MyThread05 extends Thread {

    @Override
    public void run() {
        super.run();
        try {
            for(int i = 0 ; i < 100000; i ++){
                System.out.println("i = "+(i+1));
            }
            System.out.println("run begin");
            Thread.sleep(200000);
            System.out.println("run end");
        } catch (InterruptedException e) {
            System.out.println("在沉睡中被停止！进入catch! isInterrupted: " + this.isInterrupted());
            e.printStackTrace();
        }
    }
}

class Run05 {
    public static void main(String[] args) {
        try {
            MyThread05 thread = new MyThread05();
            thread.start();
            Thread.sleep(200);  // 先sleep，再中断。
            thread.interrupt();
        } catch (InterruptedException e) {
            System.out.println("main catch");
            e.printStackTrace();
        }
    }
}
