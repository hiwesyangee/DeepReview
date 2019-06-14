package com.hiwes.cores.thread.thread3.Thread0219;

import java.util.Date;

/**
 * 再再次验证ThreadLocal的数据隔离性和初始化默认值.
 */
public class MyThread08 extends Thread {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("在ThreadA线程中取值 = " + Tools08.tl.get());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


class Tools08 {
    public static ThreadLocalExt08 tl = new ThreadLocalExt08();
}


class ThreadLocalExt08 extends ThreadLocal {

    @Override
    protected Object initialValue() {
        return new Date().getTime();
    }
}

class Run08 {
    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("        在Main线程中取值 = " + Tools08.tl.get());
                Thread.sleep(100);
            }
            Thread.sleep(2000);
            MyThread08 a = new MyThread08();
            a.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}