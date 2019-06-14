package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * interrupt结合return，实现线程的中断。
 */
public class MyThread09 extends Thread {

    @Override
    public void run() {
        while (true) {
            if (this.isInterrupted()) {
                System.out.println("被停止了！");
                return;
            }
            System.out.println("timer= " + System.currentTimeMillis());
        }
    }

}

class Run09 {
    public static void main(String[] args) {
        try {
            MyThread09 thread = new MyThread09();
            thread.start();
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}