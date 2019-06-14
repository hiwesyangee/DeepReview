package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * synchronized同步方法容易造成死循环。
 *
 * 修改方式:使用同步块，将对象锁的对象设置为不同的，从而形成不同的对象锁。避免死循环。
 */
public class MyThread19 extends Thread {


    private Service19 service;

    public MyThread19(Service19 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.methodA();
    }
}


class MyThread19_2 extends Thread {

    private Service19 service;

    public MyThread19_2(Service19 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.methodB();
    }
}

class Service19 {
    Object object1 = new Object();
    public void methodA() {
        System.out.println("methodA begin.");
        synchronized (object1) {
            boolean isContinueRun = true;
            while (isContinueRun) {  // 死循环

            }
            System.out.println("methodA end.");
        }
    }

    Object object2 = new Object();
    public void methodB() {
        synchronized (object2) {
            System.out.println("methodB begin.");
            System.out.println("methodB end.");
        }
    }
}

class Run19 {
    public static void main(String[] args) {
        Service19 service = new Service19();
        MyThread19 a = new MyThread19(service);
        a.start();
        MyThread19_2 b = new MyThread19_2(service);
        b.start();
    }
}