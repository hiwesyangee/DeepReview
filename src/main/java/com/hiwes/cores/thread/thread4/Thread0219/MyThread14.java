package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.ReentrantLock;

/**
 * boolean isFair()。
 * 判断是不是公平锁。  默认情况下ReentrantLock使用的是非公平锁。
 */
public class MyThread14 {
}

class Service14 {
    private ReentrantLock lock;

    public Service14(boolean isFair) {
        super();
        lock = new ReentrantLock(isFair);
    }

    public void serviceMethod() {
        try {
            lock.lock();
            System.out.println("是否是公平锁: " + lock.isFair());
        } finally {
            lock.unlock();
        }
    }
}


class Run14 {
    public static void main(String[] args) {
        final Service14 service = new Service14(true);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                service.serviceMethod();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        final Service14 service2 = new Service14(false);
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                service2.serviceMethod();
            }
        };
        thread = new Thread(runnable2);
        thread.start();

    }
}
