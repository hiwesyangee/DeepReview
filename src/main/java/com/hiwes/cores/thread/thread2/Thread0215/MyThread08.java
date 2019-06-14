package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 验证:使用synchronized(非this对象x)同步代码块格式时，持有不同的对象监视器，是异步的效果。
 */
public class MyThread08 extends Thread {
    private Service2 service;

    public MyThread08(Service2 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.a();
    }
}

class MyThread08_2 extends Thread {

    private Service2 service;

    public MyThread08_2(Service2 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.b();
    }
}

class Service2 {
    private String anyThing = new String();

    public void a() {
        try {
            synchronized (anyThing) {
                System.out.println("a begin");
                Thread.sleep(3000);
                System.out.println("a end");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public void b() {
        System.out.println("b begin");
        System.out.println("b end");
    }
}

class Run08 {
    public static void main(String[] args) {
        Service2 service = new Service2();
        MyThread08 thread1 = new MyThread08(service);
        thread1.setName("A");
        thread1.start();
        MyThread08_2 thread2 = new MyThread08_2(service);
        thread2.setName("B");
        thread2.start();
    }
}