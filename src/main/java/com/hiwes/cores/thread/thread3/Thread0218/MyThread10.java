package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * 如果通知过早，会打乱程序运行的顺序。
 * notify()代码无效，wait()代码永远不会被唤醒。
 */
public class MyThread10 {
    private String lock = new String("");
    private Runnable runnableA = new Runnable() {
        @Override
        public void run() {
            try {
                synchronized (lock) {
                    System.out.println("begin wait.");
                    lock.wait();
                    System.out.println("  end wait.");
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
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
//        MyThread10 run = new MyThread10();
//        Thread a = new Thread(run.runnableA);
//        a.start();
//        Thread b = new Thread(run.runnableB);
//        b.start();

        MyThread10 run = new MyThread10();
        Thread b = new Thread(run.runnableB);
        b.start();
        Thread.sleep(100);
        Thread a = new Thread(run.runnableA);
        a.start();
    }
}
