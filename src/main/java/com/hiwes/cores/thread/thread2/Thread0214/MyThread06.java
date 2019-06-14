package com.hiwes.cores.thread.thread2.Thread0214;

/**
 * 可重入锁:自可以再次获取自己的内部锁。
 * 当一个线程获得了某个对象的锁，如果这个对象锁没有得到释放，那么当期再次想要获取这个对象的锁的时候还是可以获得；
 * 否则就会造成一个问题：死锁。
 * 可重入锁也支持在父子类继承的环境中，见MyThread07.java。
 */
public class MyThread06 extends Thread {
    @Override
    public void run() {
        Service service = new Service();
        service.service1();
    }
}

class Service {
    synchronized public void service1() {
        System.out.println("setvice1");
        service2();
        System.out.println("service1 end.");
    }

    synchronized public void service2() {
        System.out.println("service2");
        service3();
        System.out.println("service2 end.");
    }

    synchronized public void service3() {
        System.out.println("service3");
        System.out.println("service3 end.");
    }
}

class Run06 {
    public static void main(String[] args) {
        MyThread06 thread = new MyThread06();
        thread.start();
    }
}

