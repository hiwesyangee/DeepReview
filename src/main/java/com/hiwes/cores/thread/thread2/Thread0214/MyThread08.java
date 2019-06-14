package com.hiwes.cores.thread.thread2.Thread0214;

/**
 * 线程A出现异常，自动释放了锁，线程B进入方法正常打印。
 */
public class MyThread08 extends Thread {

    private Service2 service;

    public MyThread08(Service2 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.testMethod();
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
        service.testMethod();
    }
}

class Service2 {
    synchronized public void testMethod() {
        if (Thread.currentThread().getName().equals("a")) {
            System.out.println("ThreadName=" + Thread.currentThread().getName() + " run beginTime= " + System.currentTimeMillis());
            int i = 1;
            while (i == 1) {
                if (("" + Math.random()).substring(0, 8).equals("0.123456")) {
                    System.out.println("ThreadName=" + Thread.currentThread().getName() + " run exceptionTime= " + System.currentTimeMillis());
                    Integer.parseInt("a"); // 主动创建一个Exception，用来释放锁。
                }
            }
        } else {
            System.out.println("Thread B run time= " + System.currentTimeMillis());
        }
    }
}

class Run08 {
    public static void main(String[] args) {
        try {
            Service2 service = new Service2();
            MyThread08 AThread = new MyThread08(service);
            AThread.setName("a");
            AThread.start();
            Thread.sleep(500);
            MyThread08_2 BThread = new MyThread08_2(service);
            BThread.setName("b");
            BThread.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}