package com.hiwes.cores.thread.thread2.Thread0218;

/**
 * 在将任何数据类型作为同步锁时，需要注意的是，是否有多个线程同时持有锁对象。
 * 如果同时持有相同的锁对象，则这些线程之间是同步的；如果分别持有锁对象，这些线程之间就是异步的。
 */
public class MyThread03 extends Thread {
    private MyService service;

    public MyThread03(MyService service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.testMethod();
    }
}

class MyThread03_2 extends Thread {
    private MyService service;

    public MyThread03_2(MyService service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.testMethod();
    }
}

class MyService {
    private String lock = "123";

    public void testMethod() {
        try {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " begin time = " + System.currentTimeMillis());
                lock = "456";
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + " end time = " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run03_1 {
    public static void main(String[] args) throws InterruptedException {
        MyService service = new MyService();
        MyThread03 a = new MyThread03(service);
        a.setName("A");
        MyThread03_2 b = new MyThread03_2(service);
        b.setName("B");
        a.start();
        Thread.sleep(50);   //存在50毫秒,此时的lock已经变更为“456”,对象改变。
        b.start();
    }
}

class Run03_2{
    public static void main(String[] args) {
        MyService service = new MyService();
        MyThread03 a = new MyThread03(service);
        a.setName("A");
        MyThread03_2 b = new MyThread03_2(service);
        b.setName("B");
        a.start();
        b.start();
    }
}