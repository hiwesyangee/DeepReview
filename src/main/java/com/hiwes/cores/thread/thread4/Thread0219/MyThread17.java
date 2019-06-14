package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * void lockInterruptibly()。
 * 如果当前线程未被中断，则获取锁定，如果已经被中断则出现异常。
 */
public class MyThread17 {
}

class Service17 {

    private ReentrantLock lock = new ReentrantLock();  // 默认使用false,非公平锁。

    private Condition condition = lock.newCondition();

    public void waitMethod() {
        try {
            lock.lock();
//            lock.lockInterruptibly();
            System.out.println("lock begin: " + Thread.currentThread().getName());
            for (int i = 0; i < Integer.MAX_VALUE / 10; i++) {
                String newString = new String();
                Math.random();
            }
            System.out.println("lock   end: " + Thread.currentThread().getName());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}


class Run17 {
    public static void main(String[] args) throws InterruptedException {
        final Service17 service = new Service17();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                service.waitMethod();
            }
        };

        Thread a = new Thread(runnable);
        a.setName("A");
        a.start();
        Thread.sleep(500);
        Thread b = new Thread(runnable);
        b.setName("B");
        b.start();
        b.interrupt(); // 打断标记
        System.out.println("main end.");
    }
}