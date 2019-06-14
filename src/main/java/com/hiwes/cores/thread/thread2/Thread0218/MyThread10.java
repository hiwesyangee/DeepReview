package com.hiwes.cores.thread.thread2.Thread0218;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用原子类进行++操作,即:原子操作。
 */
public class MyThread10 extends Thread{

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void run(){
        for(int i = 0 ; i < 10000; i ++){
            System.out.println(count.incrementAndGet());
        }
    }

}


class Run10{
    public static void main(String[] args) {
        MyThread10 countService = new MyThread10();
        Thread t1 = new Thread(countService);
        t1.start();
        Thread t2 = new Thread(countService);
        t2.start();
        Thread t3 = new Thread(countService);
        t3.start();
        Thread t4 = new Thread(countService);
        t4.start();
        Thread t5 = new Thread(countService);
        t5.start();

    }
}