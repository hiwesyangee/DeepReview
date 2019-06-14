package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * 不共享线程内的实例变量
 */
public class MyThread05 extends Thread {
    private int count = 5;

    public MyThread05(String name) {
        super();
        this.setName(name); // 设置线程名称
    }

    @Override
    public void run() {
        super.run();
        while (count > 0) {
            count--;
            System.out.println("由: " + this.currentThread().getName() + " 计算,count= " + count);
        }
    }
}

class Run05 {
    public static void main(String[] args) {
        MyThread05 a = new MyThread05("A");
        MyThread05 b = new MyThread05("B");
        MyThread05 c = new MyThread05("C");
        a.start();
        b.start();
        c.start();
    }
}