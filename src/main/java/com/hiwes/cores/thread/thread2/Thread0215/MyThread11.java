package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 验证第一个结论:
 * 当多个线程同时执行synchronized(x){ }同步代码块时呈同步效果；
 */
public class MyThread11 extends Thread {

    private Service11 service;
    private MyObject object;

    public MyThread11(Service11 service, MyObject object) {
        super();
        this.service = service;
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        service.testMethod1(object);
    }
}

class MyThread11_2 extends Thread {

    private Service11 service;
    private MyObject object;

    public MyThread11_2(Service11 service, MyObject object) {
        super();
        this.service = service;
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        service.testMethod1(object);
    }
}

class Service11 {
    public void testMethod1(MyObject object) {
        synchronized (object) {  // 使用同一个对象监视器，形成同步。
            try {
                System.out.println("testMethod1 ____getLock time= " + System.currentTimeMillis() + " run ThreadName= " + Thread.currentThread().getName());
                Thread.sleep(2000);
                System.out.println("testMethod1 releaseLock time= " + System.currentTimeMillis() + " run ThreadName= " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyObject {

}


class Run11 {
    public static void main(String[] args) {
        Service11 service = new Service11();
        MyObject object = new MyObject();
        MyThread11 a = new MyThread11(service, object);
        a.setName("A");
        a.start();
        MyThread11_2 b = new MyThread11_2(service, object);
        b.setName("B");
        b.start();
    }
}

class Run11_2 {
    public static void main(String[] args) {
        Service11 service = new Service11();
        MyObject object = new MyObject();  // 创造不同的对象，形成不同的对象监视器。
        MyObject object2 = new MyObject();

        MyThread11 a = new MyThread11(service, object);
        a.setName("A");
        a.start();
        MyThread11_2 b = new MyThread11_2(service, object2);
        b.setName("B");
        b.start();
    }
}