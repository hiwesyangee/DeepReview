package com.hiwes.cores.thread.thread2.Thread0214;

/**
 * 牢记一点，只有需要“共享”的资源进行读写访问的时候才需要进行同步化，否则，完全没有必要进行同步的必要。
 *
 * 如果是同时存在synchronized方法和asynchronized方法，那么即使是线程A先执行synchronized方法，线程B依然可以异步执行asynchronized方法。
 *
 * 那么:  A线程先持有Object对象的Lock锁，B线程可以异步调用Object对象中的非synchronized类型的方法；
 *       A线程先持有Object对象的Lock锁，B线程只能同步调用Object对象中其他的synchronized类型的方法，必须等待。
 */
public class MyThread04 extends Thread {
    private MyObject object;

    public MyThread04(MyObject object) {
        super();
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        object.methodA();
    }
}

class MyThread04_2 extends Thread {
    private MyObject object;

    public MyThread04_2(MyObject object) {
        super();
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        object.methodB();
    }
}

class MyObject {
    synchronized public void methodA() {
        try {
            System.out.println("begin methodA threadName= " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("end endTime= " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 第一个版本：没有methodB()；
     * 第二个版本：methodB()没有synchronized；
     * 第三个版本：methodB()有synchronized。
     */
    synchronized public void methodB() {
        try {
            System.out.println("begin methodB threadName= " + Thread.currentThread().getName() + " begin Time=" + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run04 {
    public static void main(String[] args) {
        MyObject object = new MyObject();
        MyThread04 a = new MyThread04(object);
        a.setName("A");
        MyThread04_2 b = new MyThread04_2(object);
        b.setName("B");
        a.start();
        b.start();
    }
}