package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用awaitUninterruptibly().前面几种会响应其他线程发出的中断请求，这个方法会无视，直到被唤醒。
 * 本例使用await()会报错。
 * 使用.awaitUninterruptibly();则正常运行。
 */
public class MyThread20 extends Thread {
    private Service20 service;

    public MyThread20(Service20 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.testMethod();
    }

}

class Service20 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void testMethod() {
        try {
            lock.lock();
            System.out.println("wait begin.");
//            condition.await();
            condition.awaitUninterruptibly();  // 无视其他线程的中断请求。
            System.out.println("wait   end.");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class Run20 {
    public static void main(String[] args) {
        try {
            Service20 service = new Service20();
            MyThread20 thread = new MyThread20(service);
            thread.start();
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}