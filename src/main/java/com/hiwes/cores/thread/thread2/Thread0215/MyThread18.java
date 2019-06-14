package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 此时，A、B线程持有的锁，并不是同一个，因为进入的是不同的对象；
 * 而不是使用String常量，因为常量池，所以指向的是同一个对象，即:锁也是同一个。
 *
 * 为了减少在JVM中创建的字符串的数量，字符串类维护了一个字符串池，
 * 每当代码创建字符串常量时，JVM会首先检查字符串常量池。
 * 如果字符串已经存在池中，就返回池中的实例引用。如果字符串不在池中，就会实例化一个字符串并放到池中。
 */
public class MyThread18 extends Thread {

    private Service18 service;

    public MyThread18(Service18 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.print(new Object());
    }
}

class MyThread18_2 extends Thread {

    private Service18 service;

    public MyThread18_2(Service18 service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.print(new Object());
    }
}


class Service18 {
    public static void print(Object object) {
        try {
            synchronized (object) {
                while (true) {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run18 {
    public static void main(String[] args) {
        Service18 service = new Service18();
        MyThread18 a = new MyThread18(service);
        a.setName("A");
        a.start();
        MyThread18_2 b = new MyThread18_2(service);
        b.setName("B");
        b.start();
    }
}