package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * notify()先唤醒，wait就不会执行。
 */
public class MyThread11 {
    private String lock = new String("");
    private boolean isFirstRunB = false;
    private Runnable runnableA = new Runnable() {
        @Override
        public void run() {
            try {
                synchronized (lock) {
                    while (isFirstRunB == false) {
                        System.out.println("begin wait.");
                        lock.wait();
                        System.out.println("  end wait.");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnableB = new Runnable() {
        @Override
        public void run() {
            synchronized (lock) {
                System.out.println("begin notify.");
                lock.notify();
                System.out.println("  end notify.");
                isFirstRunB = true;
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        MyThread11 run = new MyThread11();
//        Thread b = new Thread(run.runnableB);
//        b.start();
//        Thread.sleep(100);
//        Thread a = new Thread(run.runnableA);
//        a.start();
        Thread a = new Thread(run.runnableA);
        a.start();
        Thread.sleep(1000);
        Thread b = new Thread(run.runnableB);
        b.start();
    }
}


