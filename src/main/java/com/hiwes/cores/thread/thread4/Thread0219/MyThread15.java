package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.ReentrantLock;

/**
 * boolean isHeldByCurrentThread()。
 * 查询当前线程是否保持此锁定。
 * 可以在lock.lock()下面直接调用，从而判断是否是保持当前锁。
 */
public class MyThread15 {
}


class Service15 {
    private ReentrantLock lock;

    public Service15(boolean isFair) {
        super();
        lock = new ReentrantLock(isFair);
    }

    public void serviceMethod() {
        try {
            System.out.println(lock.isHeldByCurrentThread());
            lock.lock();
            System.out.println(lock.isHeldByCurrentThread());
        } finally {
            lock.unlock();
        }
    }

}


class Run15 {
    public static void main(String[] args) {
        final Service15 service = new Service15(true);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                service.serviceMethod();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}