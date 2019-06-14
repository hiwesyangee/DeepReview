package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * boolean tryLock(long timeout，TimeUnit unit)。
 * 如果锁定在给定等待时间内没有被另一个线程保持，且当前线程未被中断，则获得该锁定。
 */
public class MyThread19 {

}

class Service19 {
    private ReentrantLock lock = new ReentrantLock();

    public void waitMethod() {
        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) {
                System.out.println("    " + Thread.currentThread().getName() + " 获得锁的时间 " + System.currentTimeMillis());
                Thread.sleep(10000);
            } else {
                System.out.println("    " + Thread.currentThread().getName() + " 没有获得锁.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

class Run19 {
    public static void main(String[] args) {
        final Service19 service = new Service19();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 调用waitMethod时间 " + System.currentTimeMillis());
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
