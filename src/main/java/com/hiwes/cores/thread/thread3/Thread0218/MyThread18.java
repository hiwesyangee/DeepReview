package com.hiwes.cores.thread.thread3.Thread0218;

import java.util.ArrayList;
import java.util.List;

/**
 * 多生产————多消费模式:
 */
public class MyThread18 extends Thread {
    private P18 p;

    public MyThread18(P18 p) {
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

class MyThread18_2 extends Thread {
    private C18 c;

    public MyThread18_2(C18 c) {
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

class MyStack4 {
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

class P18 {
    private com.hiwes.cores.thread.thread3.Thread0218.MyStack4 MyStack4;

    public P18(com.hiwes.cores.thread.thread3.Thread0218.MyStack4 MyStack4) {
        super();
        this.MyStack4 = MyStack4;
    }

    public void pushService() {
        MyStack4.push();
    }
}

class C18 {
    private com.hiwes.cores.thread.thread3.Thread0218.MyStack4 MyStack4;

    public C18(com.hiwes.cores.thread.thread3.Thread0218.MyStack4 MyStack4) {
        super();
        this.MyStack4 = MyStack4;
    }

    public void popService() {
        System.out.println("pop = " + MyStack4.pop());
    }
}


class Run18 {
    public static void main(String[] args) {
        MyStack4 stack = new MyStack4();
        P18 p1 = new P18(stack);
        P18 p2 = new P18(stack);
        P18 p3 = new P18(stack);
        P18 p4 = new P18(stack);
        P18 p5 = new P18(stack);

        MyThread18 pThread1 = new MyThread18(p1);
        MyThread18 pThread2 = new MyThread18(p2);
        MyThread18 pThread3 = new MyThread18(p3);
        MyThread18 pThread4 = new MyThread18(p4);
        MyThread18 pThread5 = new MyThread18(p5);
        pThread1.start();
        pThread2.start();
        pThread3.start();
        pThread4.start();
        pThread5.start();

        C18 c1 = new C18(stack);
        C18 c2 = new C18(stack);
        C18 c3 = new C18(stack);
        C18 c4 = new C18(stack);
        C18 c5 = new C18(stack);
        MyThread18_2 cThread1 = new MyThread18_2(c1);
        MyThread18_2 cThread2 = new MyThread18_2(c2);
        MyThread18_2 cThread3 = new MyThread18_2(c3);
        MyThread18_2 cThread4 = new MyThread18_2(c4);
        MyThread18_2 cThread5 = new MyThread18_2(c5);
        cThread1.start();
        cThread2.start();
        cThread3.start();
        cThread4.start();
        cThread5.start();

    }
}
