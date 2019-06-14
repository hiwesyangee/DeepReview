package com.hiwes.cores.thread.thread3.Thread0219;

/**
 * ThreadLocal的实验类.
 */
public class MyThreadTest2 {
    public static ThreadLocal t1 = new ThreadLocal();

    public static void main(String[] args) {
        if (t1.get() == null) {
            System.out.println("从未放过值.");
            t1.set("我的值");
        }
        System.out.println(t1.get());
        System.out.println(t1.get());
    }
}
