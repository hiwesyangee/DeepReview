package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 2）方法int getQueueLength().
 * 返回正等待获取此锁定的线程估计数，也就是有几个在等锁释放.
 */
public class MyThread10 {

}

class Service10 {
    public ReentrantLock lock = new ReentrantLock();

    public void serviceMethod1() {
        try {
            lock.lock();
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " 进入方法!");
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}


class Run10 {
    public static void main(String[] args) throws InterruptedException {
        final Service10 service = new Service10();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                service.serviceMethod1();
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
        System.out.println("有线程数: " + service.lock.getQueueLength() + " 在等待获取锁！");
    }
}