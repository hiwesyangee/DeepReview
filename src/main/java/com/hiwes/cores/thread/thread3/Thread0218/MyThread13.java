package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * 实现set和get交替运行，从而实现生产者/消费者模式。
 */
public class MyThread13 extends Thread {
    private P p;

    public MyThread13(P p) {
        super();
        this.p = p;
    }

    @Override
    public void run() {
        while (true) {
            p.setValue();
        }
    }
}

class MyThread13_2 extends Thread {
    private C c;

    public MyThread13_2(C c) {
        super();
        this.c = c;
    }

    @Override
    public void run() {
        while (true) {
            c.getValue();
        }
    }
}

/**
 * 生产者p
 */

class P {
    private String lock;

    public P(String lock) {
        super();
        this.lock = lock;
    }

    public void setValue() {
        try {
            synchronized (lock) {
                if (!ValueObject2.value.equals("")) {
                    lock.wait();
                }
                String value = System.currentTimeMillis() + "_" + System.nanoTime();
                System.out.println("set的值为:" + value);
                ValueObject2.value = value;
                lock.notify();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 消费者类
 */
class C {
    private String lock;

    public C(String lock) {
        super();
        this.lock = lock;
    }

    public void getValue() {
        try {
            synchronized (lock) {
                if (ValueObject2.value.equals("")) {
                    lock.wait();
                }
                System.out.println("get 的值是:" + ValueObject2.value);
                ValueObject2.value = "";
                lock.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ValueObject2 {
    public static String value = "";
}

class Run13 {
    public static void main(String[] args) {
        String lock = new String("");
        P p = new P(lock);
        C c = new C(lock);
        MyThread13 pThread = new MyThread13(p);
        MyThread13_2 cThread = new MyThread13_2(c);
        pThread.start();
        cThread.start();
    }
}