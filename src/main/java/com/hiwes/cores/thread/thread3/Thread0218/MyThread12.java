package com.hiwes.cores.thread.thread3.Thread0218;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用wait/notify模式时，wait等待的条件发生了变化，也容易造成程序逻辑的混乱。
 * 出现了两个实现删除remove()操作的线程，但是在Thread.sleep(1000)之前，都执行了wait()状态。
 * 当加操作线程在1s后被执行时，通知了所有进行等待的减操作线程，第一个实现减操作的能正确删除list中索引为0的数据，
 * 但第二个线程就会出现索引溢出的异常。因为list中只有一个元素可以删除。
 */
public class MyThread12 extends Thread {
    private Add p;

    public MyThread12(Add p) {
        super();
        this.p = p;
    }

    @Override
    public void run() {
        p.add();
    }
}

class MyThread12_2 extends Thread {
    private Subtract2 r;

    public MyThread12_2(Subtract2 r) {
        super();
        this.r = r;
    }

    @Override
    public void run() {
        r.subtract();
    }
}

class Add {
    private String lock;

    public Add(String lock) {
        super();
        this.lock = lock;
    }

    public void add() {
        synchronized (lock) {
            ValueObject.list.add("anyThing.");
            lock.notifyAll();
        }
    }
}

class Subtract {
    private String lock;

    public Subtract(String lock) {
        super();
        this.lock = lock;
    }

    public void subtract() {
        try {
            synchronized (lock) {
                if (ValueObject.list.size() == 0) {
                    System.out.println("wait begin ThreadName = " + Thread.currentThread().getName());
                    lock.wait();
                    System.out.println("wait   end ThreadName = " + Thread.currentThread().getName());
                }
                ValueObject.list.remove(0);
                System.out.println("list size = " + ValueObject.list.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class ValueObject {
    public static List list = new ArrayList();
}

class Run12 {
    public static void main(String[] args) throws InterruptedException {
//        String lock = new String("");
//        Add add = new Add(lock);
//        Subtract subtract = new Subtract(lock);
//        MyThread12_2 sub = new MyThread12_2(subtract);
//        sub.setName("Sub");
//        sub.start();
//        MyThread12_2 sub2 = new MyThread12_2(subtract);
//        sub2.setName("Sub2");
//        sub2.start();
//        Thread.sleep(1000);
//        MyThread12 addThread = new MyThread12(add);
//        addThread.setName("Add");
//        addThread.start();
    }
}


class Subtract2 {
    private String lock;

    public Subtract2(String lock) {
        super();
        this.lock = lock;
    }

    public void subtract() {
        try {
            synchronized (lock) {
                while (ValueObject.list.size() == 0) {
                    System.out.println("wait begin ThreadName = " + Thread.currentThread().getName());
                    lock.wait();
                    System.out.println("wait   end ThreadName = " + Thread.currentThread().getName());
                }
                ValueObject.list.remove(0);
                System.out.println("list value = " + ValueObject.list.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run12_2 {
    public static void main(String[] args) throws InterruptedException {
        String lock = new String("");
        Add add = new Add(lock);
        Subtract2 subtract = new Subtract2(lock);
        MyThread12_2 sub = new MyThread12_2(subtract);
        sub.setName("Sub");
        sub.start();
        MyThread12_2 sub2 = new MyThread12_2(subtract);
        sub2.setName("Sub2");
        sub2.start();
        Thread.sleep(1000);
        MyThread12 addThread = new MyThread12(add);
        addThread.setName("Add");
        addThread.start();
    }
}