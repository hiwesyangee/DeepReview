package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * int getWaitQueueLength(Condition condition)。
 * 返回等待与此锁定相关的给定条件condition的线程估计数。
 */
public class MyThread11 {
}

class Service11 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition newCondition = lock.newCondition();

    public void waitMethod() {
        try {
            lock.lock();
            newCondition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.lock();
        }
    }

    public void notifyMethod() {
        try {
            lock.lock();
            System.out.println("有 " + lock.getWaitQueueLength(newCondition) + " 个线程正在等待newCondition.");
            newCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}


class Run11 {
    public static void main(String[] args) throws InterruptedException {
        final Service11 service = new Service11();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                service.waitMethod();
            }
        };
        Thread[] threadArray = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threadArray[i] = new Thread(runnable);
        }

        for (int i = 0; i < 10; i++) {
            threadArray[i].start();
        }
        Thread.sleep(2000);
        service.notifyMethod();
    }
}
