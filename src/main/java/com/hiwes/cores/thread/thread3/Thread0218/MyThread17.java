package com.hiwes.cores.thread.thread3.Thread0218;

import java.util.ArrayList;
import java.util.List;

/**
 * 多生产————一消费模式:
 * 注意：在push时，如果使用if而不是while，会不断的进行生产，从而产生大量的对象。
 */
public class MyThread17 extends Thread {
    private P17 p;

    public MyThread17(P17 p) {
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

class MyThread17_2 extends Thread {
    private C17 c;

    public MyThread17_2(C17 c) {
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

class MyStack3 {
    private List list = new ArrayList();

    synchronized public void push() {
        try {
            while (list.size() == 1) {
                this.wait();
            }
            list.add("anyThing = " + Math.random());
//            this.notify();  // 还是出现了假死情况
            this.notifyAll();
            System.out.println("push = " + list.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public String pop() {
        String returnValue = "";
        try {
            /**
             * 因为条件发生改变时并没有得到及时的相应，所以多个呈waigt状态的线程被唤醒,继而执行remove操作出现异常。
             */
//            if (list.size() == 0) {
            while (list.size() == 0) {
                System.out.println("pop操作中的: " + Thread.currentThread().getName() + " 线程呈wait状态.");
                this.wait();
            }
            returnValue = "" + list.get(0);
            list.remove(0);
//            this.notify();
            this.notifyAll();
            System.out.println("pop = " + list.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}

class P17 {
    private com.hiwes.cores.thread.thread3.Thread0218.MyStack3 MyStack3;

    public P17(com.hiwes.cores.thread.thread3.Thread0218.MyStack3 MyStack3) {
        super();
        this.MyStack3 = MyStack3;
    }

    public void pushService() {
        MyStack3.push();
    }
}

class C17 {
    private com.hiwes.cores.thread.thread3.Thread0218.MyStack3 MyStack3;

    public C17(com.hiwes.cores.thread.thread3.Thread0218.MyStack3 MyStack3) {
        super();
        this.MyStack3 = MyStack3;
    }

    public void popService() {
        System.out.println("pop = " + MyStack3.pop());
    }
}


class Run17 {
    public static void main(String[] args) {
        MyStack3 stack = new MyStack3();
        P17 p1 = new P17(stack);
        P17 p2 = new P17(stack);
        P17 p3 = new P17(stack);
        P17 p4 = new P17(stack);
        P17 p5 = new P17(stack);

        MyThread17 pThread1 = new MyThread17(p1);
        MyThread17 pThread2 = new MyThread17(p2);
        MyThread17 pThread3 = new MyThread17(p3);
        MyThread17 pThread4 = new MyThread17(p4);
        MyThread17 pThread5 = new MyThread17(p5);
        pThread1.start();
        pThread2.start();
        pThread3.start();
        pThread4.start();
        pThread5.start();

        C17 c = new C17(stack);
        MyThread17_2 cThread = new MyThread17_2(c);
        cThread.start();
    }
}



