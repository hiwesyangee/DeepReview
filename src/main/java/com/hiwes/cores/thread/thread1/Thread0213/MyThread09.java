package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * 测试currentThread的复杂情况
 */
public class MyThread09 extends Thread {
    public MyThread09(){
        System.out.println("CountOperate---begin");
        System.out.println("Thread.currentThread().getName = "+Thread.currentThread().getName());
        System.out.println("this.getName() = "+this.getName());
        System.out.println("CountOperate---end");
    }

    @Override
    public void run(){
        System.out.println("run---begin");
        System.out.println("Thread.currentThread().getName = "+Thread.currentThread().getName());
        System.out.println("this.getName() = "+this.getName());
        System.out.println("run---end");
    }
}

class Run09{
    public static void main(String[] args) {
        MyThread09 thread = new MyThread09();
        Thread r1 = new Thread(thread);
        r1.setName("A");
        r1.start();
    }
}