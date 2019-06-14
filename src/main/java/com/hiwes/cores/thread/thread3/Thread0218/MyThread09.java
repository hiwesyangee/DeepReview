package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * 使用wait(long)，等待某一时间内是否有线程对锁进行唤醒，超出时间则自动唤醒
 */
public class MyThread09 {
    static private Object lock = new Object();
    static private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            try {
                synchronized (lock) {
                    System.out.println("wait begin timer = " + System.currentTimeMillis());
                    lock.wait(5000);  // 等待5s被唤醒，超过就自动唤醒。
                    System.out.println("wait   end timer = " + System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) {
        Thread t = new Thread(runnable1);
        t.start();
    }
}

class MyThread09_2 {
    static private Object lock = new Object();
    static private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            try {
                synchronized (lock) {
                    System.out.println("wait begin timer = " + System.currentTimeMillis());
                    lock.wait(5000);
                    System.out.println("wait   end timer = " + System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    static private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            synchronized (lock) {
                System.out.println("wait begin timer = " + System.currentTimeMillis());
                lock.notify();
                System.out.println("wait   end timer = " + System.currentTimeMillis());
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(runnable1);
        t1.start();
        Thread.sleep(3000);
        Thread t2 = new Thread(runnable2);
        t2.start();
    }
}