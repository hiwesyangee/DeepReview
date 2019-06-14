package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.ReentrantLock;

/**
 * boolean isLocked()。
 * 查询此锁定是否由任意线程保持。
 */
public class MyThread16 {

}

class Service16 {
    private ReentrantLock lock;

    public Service16(boolean isFair) {
        super();
        lock = new ReentrantLock(isFair);
    }

    public void serviceMethod() {
        try {
            System.out.println(lock.isLocked());
            lock.lock();
            System.out.println(lock.isLocked());
        } finally {
            lock.unlock();
        }
    }
}

class Run16 {
    public static void main(String[] args) {
        final Service16 service = new Service16(true);
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