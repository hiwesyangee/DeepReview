package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 1）方法int getHoldCount().
 * 查询当前线程保持此锁定的个数，也就是调用lock()方法的次数。
 */
public class MyThread09 {

}

class Service9{
    private ReentrantLock lock = new ReentrantLock();
    public void serviceMethod1(){
        try{
            lock.lock();
            System.out.println("serviceMethod1 getHoldCount = "+lock.getHoldCount());
            serviceMethod2();
        }finally {
            lock.unlock();
            System.out.println("finally serviceMethod1 getHoldCount = "+lock.getHoldCount());
        }
    }

    public void serviceMethod2(){
        try{
            lock.lock();
            System.out.println("serviceMethod2 getHoldCount = "+lock.getHoldCount());

        }finally {
            lock.unlock();
        }
    }
}


class Run09{
    public static void main(String[] args) {
        Service9 service = new Service9();
        service.serviceMethod1();
    }
}