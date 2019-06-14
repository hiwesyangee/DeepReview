package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 另一种独占锁的情况：
 * 1.当i++的时候，i一直在自增。
 * 2.当System.out.println(i)的时候，当程序运行到println()方法内部停止时，同步锁未被释放。
 * 从而导致当前PrintStream对象的println()方法一直呈“暂停”状态，并且“锁未释放”，造成System.out.println(“main end”)一直无法打印。
 */
public class MyThread12 extends Thread {

    private long i = 0;

    @Override
    public void run() {
        while (true) {
            i++;
            System.out.println(i);  // 当运行到println()内部时被停止，同步锁没有得到释放。
        }
    }
}


class Run12 {
    public static void main(String[] args) {
        try {
            MyThread12 thread = new MyThread12();
            thread.start();
            Thread.sleep(1000);
            thread.suspend();   // 这里没有得到释放。
            /**
             * 如果加上下面两条，则会释放同步锁，从而进入死循环，且依然不会打印main end！
             */
            Thread.sleep(2000);
            thread.resume();
            /** ========================= */
            System.out.println("main end!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

