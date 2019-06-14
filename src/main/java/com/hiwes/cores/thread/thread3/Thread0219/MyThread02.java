package com.hiwes.cores.thread.thread3.Thread0219;

/**
 * 在join()过程中，如果当前线程对象被中断，则当前线程出现异常。
 * join和interrupt彼此遇到，则会出现异常。
 * 但是进程依然运行，因为线程A还在继续执行，A并未出现异常，是正常执行的状态。
 */
public class MyThread02 extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String newString = new String();
            Math.random();
        }
    }
}


class MyThread02_2 extends Thread {

    @Override
    public void run() {
        try {
            MyThread02 a = new MyThread02();
            a.start();  // 此时，a线程一直在执行。
            a.join();   // 此时，b线程一直在等待a线程执行。
            System.out.println("线程b在run end处打印了.");
        } catch (InterruptedException e) {
            System.out.println("线程b在catch处打印了.");
            e.printStackTrace();
        }
    }
}


class MyThread02_3 extends Thread {

    private MyThread02_2 thread2;

    public MyThread02_3(MyThread02_2 thread2) {
        super();
        this.thread2 = thread2;
    }

    @Override
    public void run() {
        thread2.interrupt();  // 此时，当前线程是线程b，对象调用了interrupt。
    }
}


class Run02 {
    public static void main(String[] args) {
        try {
            MyThread02_2 b = new MyThread02_2();
            b.start();
            Thread.sleep(500);
            MyThread02_3 c = new MyThread02_3(b);
            c.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}