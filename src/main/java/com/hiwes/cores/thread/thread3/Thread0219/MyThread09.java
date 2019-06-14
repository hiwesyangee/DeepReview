package com.hiwes.cores.thread.thread3.Thread0219;

import java.util.Date;

/**
 * 使用InheritableThreadLocal，在子线程获取父线程继承下来的值.
 */
public class MyThread09 extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("ThreadA中取得值 = " + Tools09.tl.get());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class InheritableThreadLocalExt extends InheritableThreadLocal {

    @Override
    protected Object initialValue() {
        return new Date().getTime();
    }
}

class Tools09 {
    public static InheritableThreadLocalExt tl = new InheritableThreadLocalExt();
}


class Run09 {
    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("        Main线程中取得值 = " + Tools09.tl.get());
                Thread.sleep(100);
            }
            Thread.sleep(2000);
            MyThread09 a = new MyThread09();
            a.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}