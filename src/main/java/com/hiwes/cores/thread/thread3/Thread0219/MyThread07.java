package com.hiwes.cores.thread.thread3.Thread0219;

import java.util.Date;

/**
 * 再次验证ThreadLocal的数据隔离性。
 */
public class MyThread07 extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 20; i++) {
                if (Tools2.tl.get() == null) {
                    Tools2.tl.set(new Date());
                }
                System.out.println("A " + Tools2.tl.get().getTime());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class MyThread07_2 extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 20; i++) {
                if (Tools2.tl.get() == null) {
                    Tools2.tl.set(new Date());
                }
                System.out.println("B " + Tools2.tl.get().getTime());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Tools2 {
    public static ThreadLocal<Date> tl = new ThreadLocal<>();
}

class Run07 {
    public static void main(String[] args) {
        try {
            MyThread07 a = new MyThread07();
            a.start();
            Thread.sleep(1000);
            MyThread07_2 b = new MyThread07_2();
            b.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class ThreadLocalExt extends ThreadLocal {

    @Override
    protected Object initialValue() {
        return "默认值 第一次get不再是null";
    }
}

class Run07_2 {
    public static ThreadLocalExt tl2 = new ThreadLocalExt();

    public static void main(String[] args) {
        if (tl2.get() == null) {
            System.out.println("从未放过值.");
            tl2.set("我的值");
        }
        System.out.println(tl2.get());
        System.out.println(tl2.get());
    }
}