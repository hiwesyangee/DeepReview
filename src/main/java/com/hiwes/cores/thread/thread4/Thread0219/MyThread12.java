package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * boolean hasQueuedThread(Thread thread)。
 * 查询指定的线程是否正在等待获取此锁定。
 * boolean hasQueuedThreads()。
 * 查询所有线程中是否正在等待获取此锁定。
 */
public class MyThread12 {
}

class Service12 {
    public ReentrantLock lock = new ReentrantLock();
    public Condition newCondition = lock.newCondition();

    public void waitMethod() {
        try {
            lock.lock();
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class Run12 {
    public static void main(String[] args) throws InterruptedException {
        final Service12 service = new Service12();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                service.waitMethod();
            }
        };
        Thread a = new Thread(runnable);
        a.start();
        Thread.sleep(500);

        Thread b = new Thread(runnable);
        b.start();
        Thread.sleep(500);

        System.out.println(service.lock.hasQueuedThread(a));
        System.out.println(service.lock.hasQueuedThread(b));
        System.out.println(service.lock.hasQueuedThreads());
    }
}