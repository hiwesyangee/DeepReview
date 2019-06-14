package com.hiwes.cores.thread.thread2.Thread0218;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 但是原子类也并不完全安全:
 * 在具有有逻辑性的情况下的输出结果也是随机性的。
 * 打印顺序出错，应该每加一次100，再加一次1。
 */
public class MyThread11 extends Thread {
    private MyService11 service;

    public MyThread11(MyService11 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.addNum();
    }
}

class MyService11 {
    public static AtomicLong aiRef = new AtomicLong();

    //    public void addNum() {  // 不使用synchronized同步就会出现顺序错误
    synchronized public void addNum() {
        System.out.println(Thread.currentThread().getName() + "加了100之后的值是: " + aiRef.addAndGet(100));
        aiRef.addAndGet(1);
    }
}

class Run11 {
    public static void main(String[] args) {
        try {
            MyService11 service = new MyService11();
            MyThread11[] array = new MyThread11[5];
            for (int i = 0; i < array.length; i++) {
                array[i] = new MyThread11(service);
            }
            for (int i = 0; i < array.length; i++) {
                array[i].start();
            }
            Thread.sleep(1000);
            System.out.println(service.aiRef.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
