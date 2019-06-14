package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 在使用同步代码块的时候需要注意：
 * 如果有多个同步代码块，当一个线程访问Object的其中一个同步代码块的时候，
 * 其他线程对同一个Object的所有其他synchronized(this)同步代码块的访问都将被阻塞。
 * 说明:synchronized使用的“对象监视器”是同一个。
 */
public class MyThread05 extends Thread {

    private ObjectService2 service;

    public MyThread05(ObjectService2 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceMethodA();
    }

}

class MyThread05_2 extends Thread {
    private ObjectService2 service;

    public MyThread05_2(ObjectService2 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceMethodB();
    }

}

class ObjectService2 {
    public void serviceMethodA() {
        try {
            synchronized (this) {
                System.out.println("A begin time = " + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("A end   end  = " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void serviceMethodB() {
        synchronized (this) {
            System.out.println("B begin time = " + System.currentTimeMillis());
            System.out.println("B end   end  = " + System.currentTimeMillis());
        }
    }
}

class Run05 {
    public static void main(String[] args) {
        ObjectService2 service = new ObjectService2();
        MyThread05 thread1 = new MyThread05(service);
        thread1.setName("a");
        thread1.start();
        MyThread05_2 thread2 = new MyThread05_2(service);
        thread2.setName("b");
        thread2.start();
    }
}
