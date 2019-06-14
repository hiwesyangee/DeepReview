package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 判断线程是否是停止状态
 * 使用this.interrupted()，测试当前线程是否已经中断。
 * // 使用interrupt()，并未中断当前线程main。
 * <p>
 * 使用this.isInterrupted()，测试线程是否被中断。
 */
public class MyThread02 extends Thread {
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 5000; i++) {
            System.out.println("i = " + (i + 1));
        }
    }
}

class Run02 {
    public static void main(String[] args) {
        try {
            MyThread02 thread = new MyThread02();
            thread.start();
            Thread.sleep(2000);
            thread.interrupt();
            System.out.println("是否停止1: " + thread.interrupted());
            System.out.println("是否停止2: " + thread.interrupted());
        } catch (InterruptedException e) {
            System.out.println("main catch");
            e.printStackTrace();
        }
    }
}

class Run02_1 {
    public static void main(String[] args) {
        Thread.currentThread().interrupt();
        System.out.println("是否停止1: " + Thread.interrupted());
        System.out.println("是否停止2: " + Thread.interrupted());
        System.out.println("end!");
    }
}

/**
 * 输出结果:
 * <p>
 * 是否停止1: true
 * 是否停止2: false
 * end!
 * <p>
 * 原因：在官方帮助文档写的很清楚：interrupted()方法，测试当前线程是否被中断，中断的状态由该方法清楚。
 * 即：两次调用该方法，第二次就会返回false。因为在第一次调用的时候已经清除了其中断状态，除非在第二次调用之前又被中断。
 */


/**
 * 使用：isInterrupted()方法:
 * 并未清除线程的状态标志。
 */
class Run02_2 {
    public static void main(String[] args) {
        try {
            MyThread03 thread = new MyThread03();
            thread.start();
            Thread.sleep(1000);
            thread.interrupt();
            System.out.println("是否停止1: " + thread.isInterrupted());
            System.out.println("是否停止2: " + thread.isInterrupted());
        } catch (InterruptedException e) {
            System.out.println("main match");
            e.printStackTrace();
        }
    }
}

/**
 * 输出结果:
 * 是否停止1: false
 * 是否停止2: false
 */

/**
 * 总结:
 * this.interrupted()。测试当前线程是否已经是中断状态，执行后具有将状态标志清除为false的功能；
 * this.isInterrupted()。测试线程Thread对象是否已经是中断状态，但是不清除状态标志。
 */