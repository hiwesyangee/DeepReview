package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 验证第二个结论:
 * 当其他线程执行X对象中synchronized同步方法时呈同步效果；
 */
public class MyThread12 extends Thread {

    private Service12 service;
    private MyObject2 object;

    public MyThread12(Service12 service, MyObject2 object) {
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

class MyThread12_2 extends Thread {

    private MyObject2 object;

    public MyThread12_2(MyObject2 object) {
        super();
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        object.speedPrintString();
    }
}

class MyObject2 {
    synchronized public void speedPrintString() {
        System.out.println("speedPrintString ____getLock time = " + System.currentTimeMillis() + " run ThreadName = " + Thread.currentThread().getName());
        System.out.println("------------------------");
        System.out.println("speedPrintString releaseLock time = " + System.currentTimeMillis() + " run ThreadName = " + Thread.currentThread().getName());
    }
}

class Service12 {
    public void testMethod1(MyObject2 object) {
        synchronized (object) {
            try {
                System.out.println("testMethod1 ____getLock time = " + System.currentTimeMillis() + " run ThreadName = " + Thread.currentThread().getName());
                Thread.sleep(5000);
                System.out.println("testMethod2 releaseLock time = " + System.currentTimeMillis() + " run ThreadName = " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


class Run12 {
    public static void main(String[] args) throws InterruptedException {
        Service12 service = new Service12();
        MyObject2 object = new MyObject2();

        MyThread12 a = new MyThread12(service, object);
        a.setName("A");
        a.start();
        Thread.sleep(100);
        MyThread12_2 b = new MyThread12_2(object);
        b.setName("B");
        b.start();

    }
}