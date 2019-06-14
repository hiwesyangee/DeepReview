package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * boolean hasWaiters(Condition condition)。
 * 查询是否有线程正在等待与此锁定有关的condition条件。
 */
public class MyThread13 {

}

class Service13 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition newCondition = lock.newCondition();

    public void waitMethod() {
        try {
            lock.lock();
            newCondition.await();  // 后续并未被唤醒.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void notifyMethod() {
        try {
            lock.lock();
            System.out.println("有没有线程正在等待newCondition? " + lock.hasWaiters(newCondition) + ". 线程数是多少? " + lock.getWaitQueueLength(newCondition));
            newCondition.signal();
        } finally {
            lock.unlock();
        }
    }
}


class Run13 {
    public static void main(String[] args) throws InterruptedException {
        final Service13 service = new Service13();
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