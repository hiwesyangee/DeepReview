package com.hiwes.cores.thread.thread3.Thread0219;

import java.util.Date;

/**
 * 在继承的同时对值进行修改.重写继承方法，childValue(Object parentValue)
 */
public class MyThread10 extends Thread {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("ThreadA中取得值 = " + Tools10.tl.get());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class InheritableThreadLocalExt2 extends InheritableThreadLocal {

    @Override
    protected Object initialValue() {
        return new Date().getTime();
    }

    /**
     * 重写继承方法，childValue(Object parentValue)，从而在父线程值的基础上进行修改.
     *
     * @param parentValue
     * @return
     */
    @Override
    protected Object childValue(Object parentValue) {
        return parentValue + " 我在子线程加的~";
    }
}

class Tools10 {
    public static InheritableThreadLocalExt2 tl = new InheritableThreadLocalExt2();
}


class Run10 {
    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("        Main线程中取得值 = " + Tools10.tl.get());
                Thread.sleep(100);
            }
            Thread.sleep(2000);
            MyThread10 a = new MyThread10();
            a.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
