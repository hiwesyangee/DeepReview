package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 验证第三个结论:
 * 当其他线程执行X对象方法里面的synchronized(this)代码块时呈同步效果；
 */
public class MyThread13 extends Thread {

    private Service13 service;
    private MyObject3 object;

    public MyThread13(Service13 service, MyObject3 object) {
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


class MyThread13_2 extends Thread {

    private MyObject3 object;

    public MyThread13_2(MyObject3 object) {
        super();
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        object.speedPrintString();
    }
}

class MyObject3 {
    public void speedPrintString() {
        synchronized (this) {  // 执行MyObject对象中的synchronized(this)方法，也会呈现同步效果。
            System.out.println("speedPrintString ____getLock time = " + System.currentTimeMillis() + " run ThreadName = " + Thread.currentThread().getName());
            System.out.println("---------------------------");
            System.out.println("speedPrintString releaseLock time = " + System.currentTimeMillis() + " run ThreadName = " + Thread.currentThread().getName());
        }
    }
}

class Service13 {
    public void testMethod1(MyObject3 object) {
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

class Run13 {
    public static void main(String[] args) throws InterruptedException {
        Service13 service = new Service13();
        MyObject3 object = new MyObject3();

        MyThread13 a = new MyThread13(service, object);
        a.setName("A");
        a.start();
        Thread.sleep(100);
        MyThread13_2 b = new MyThread13_2(object);
        b.setName("B");
        b.start();

    }
}