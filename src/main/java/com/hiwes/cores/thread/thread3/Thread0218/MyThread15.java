package com.hiwes.cores.thread.thread3.Thread0218;

import java.util.ArrayList;
import java.util.List;

/**
 * 使生产者向堆栈List对象中放入数据，使消费者从List对战中取出数据。
 * List最大容量为1，本例中只有一个生产者，一个消费者。
 * 1p1c模式。
 */
public class MyThread15 extends Thread {
    private P15 p;

    public MyThread15(P15 p) {
        super();
        this.p = p;
    }

    @Override
    public void run() {
        while (true) {
            p.pushService();
        }
    }
}

class MyThread15_2 extends Thread {
    private C15 c;

    public MyThread15_2(C15 c) {
        super();
        this.c = c;
    }

    @Override
    public void run() {
        while (true) {
            c.popService();
        }
    }
}

class MyStack {
    private List list = new ArrayList();

    synchronized public void push() {
        try {
            if (list.size() == 1) {
                this.wait();
            }
            list.add("anyThing = " + Math.random());
            this.notify();
            System.out.println("push = " + list.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public String pop() {
        String returnValue = "";
        try {
            if (list.size() == 0) {
                System.out.println("pop操作中的: " + Thread.currentThread().getName() + " 线程呈wait状态.");
                this.wait();
            }
            returnValue = "" + list.get(0);
            list.remove(0);
            this.notify();
            System.out.println("pop = " + list.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}

class P15 {
    private MyStack myStack;

    public P15(MyStack myStack) {
        super();
        this.myStack = myStack;
    }

    public void pushService() {
        myStack.push();
    }
}

class C15 {
    private MyStack myStack;

    public C15(MyStack myStack) {
        super();
        this.myStack = myStack;
    }

    public void popService() {
        System.out.println("pop = " + myStack.pop());
    }
}


class Run15 {
    public static void main(String[] args) {
        MyStack myStack = new MyStack();
        P15 p = new P15(myStack);
        C15 c = new C15(myStack);

        MyThread15 pThread = new MyThread15(p);
        MyThread15_2 cThread = new MyThread15_2(c);
        pThread.start();
        cThread.start();
    }
}