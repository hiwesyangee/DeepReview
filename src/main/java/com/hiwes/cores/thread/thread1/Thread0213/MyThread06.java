package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * 共享线程内的实例变量
 */
public class MyThread06 extends Thread {
    private int count = 5;

    @Override
    synchronized public void run() {
        super.run();
        count--;
        // 不使用for()循环，因为使用同步后，其他线程就得不到运行的机会，从而由一个线程进行减法运算。
        System.out.println("由: " + this.currentThread().getName() + " 计算,count= " + count);
    }
}

class Run06 {
    public static void main(String[] args) {
        MyThread06 myThread = new MyThread06();
        Thread a = new Thread(myThread, "A");
        Thread b = new Thread(myThread, "B");
        Thread c = new Thread(myThread, "C");
        Thread d = new Thread(myThread, "D");
        Thread e = new Thread(myThread, "E");
        a.start();
        b.start();
        c.start();
        d.start();
        e.start();
    }
}
