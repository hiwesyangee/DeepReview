package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 使用synchronized同步代码块:
 * 当两个并发线程访问同一个对象Object中的synchronized(this)同步代码块时，一段时间内只能有一个线程被执行，
 * 另一个线程必须等待当前线程执行完这个代码块之后才能执行这个代码块。
 */
public class MyThread02 extends Thread {

    private ObjectService service;

    public MyThread02(ObjectService service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceMethod();
    }
}

class MyThread02_2 extends Thread {

    private ObjectService service;

    public MyThread02_2(ObjectService service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serviceMethod();
    }
}

class ObjectService {
    public void serviceMethod() {
        try {
            synchronized (this) { // 同步代码块，使先执行的线程必须执行完这一块代码块之后才能让其他线程执行。
                System.out.println("begin time = " + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println(" end Time  = " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run02 {
    public static void main(String[] args) {
        ObjectService service = new ObjectService();
        MyThread02 thread1 = new MyThread02(service);
        thread1.setName("a");
        thread1.start();
        MyThread02_2 thread2 = new MyThread02_2(service);
        thread2.setName("b");
        thread2.start();
    }
}