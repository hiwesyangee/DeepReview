package com.hiwes.cores.thread.thread3.Thread0218;

import java.util.ArrayList;
import java.util.List;

/**
 * 一生产————多消费模式，容易造成堵塞和假死，需要灵活使用while循环和notifyAll()方法。
 */
public class MyThread16 extends Thread {
    private P16 p;

    public MyThread16(P16 p) {
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

class MyThread16_2 extends Thread {
    private C16 c;

    public MyThread16_2(C16 c) {
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

class MyStack2 {
    private List list = new ArrayList();

    synchronized public void push() {
        try {
            if (list.size() == 1) {
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

class P16 {
    private com.hiwes.cores.thread.thread3.Thread0218.MyStack2 MyStack2;

    public P16(com.hiwes.cores.thread.thread3.Thread0218.MyStack2 MyStack2) {
        super();
        this.MyStack2 = MyStack2;
    }

    public void pushService() {
        MyStack2.push();
    }
}

class C16 {
    private com.hiwes.cores.thread.thread3.Thread0218.MyStack2 MyStack2;

    public C16(com.hiwes.cores.thread.thread3.Thread0218.MyStack2 MyStack2) {
        super();
        this.MyStack2 = MyStack2;
    }

    public void popService() {
        System.out.println("pop = " + MyStack2.pop());
    }
}


class Run16 {
    public static void main(String[] args) {
        MyStack2 stack = new MyStack2();
        P16 p = new P16(stack);
        C16 c1 = new C16(stack);
        C16 c2 = new C16(stack);
        C16 c3 = new C16(stack);
        C16 c4 = new C16(stack);
        C16 c5 = new C16(stack);

        MyThread16 pThread = new MyThread16(p);
        pThread.start();
        MyThread16_2 cThread1 = new MyThread16_2(c1);
        MyThread16_2 cThread2 = new MyThread16_2(c2);
        MyThread16_2 cThread3 = new MyThread16_2(c3);
        MyThread16_2 cThread4 = new MyThread16_2(c4);
        MyThread16_2 cThread5 = new MyThread16_2(c5);

        cThread1.start();
        cThread2.start();
        cThread3.start();
        cThread4.start();
        cThread5.start();
    }
}