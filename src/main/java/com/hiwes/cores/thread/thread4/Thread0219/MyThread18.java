package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.ReentrantLock;

/**
 * boolean tryLock()。
 * 仅在调用时锁定未被另一个线程保持的情况下，才获取该锁定。
 */
public class MyThread18 {
}

class Service18 {
    private ReentrantLock lock = new ReentrantLock();

    public void waitMethod() {
        if (lock.tryLock()) {  // 没有别人用的时候，才能用。
            System.out.println(Thread.currentThread().getName() + " 获得锁.");
        } else {
            System.out.println(Thread.currentThread().getName() + " 没有获得锁.");
        }
    }

}

class Run18 {
    public static void main(String[] args) {
        final Service18 service = new Service18();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                service.waitMethod();
            }
        };

        Thread a = new Thread(runnable);
        a.setName("A");
        a.start();
        Thread b = new Thread(runnable);
        b.setName("B");
        b.start();
    }
}
