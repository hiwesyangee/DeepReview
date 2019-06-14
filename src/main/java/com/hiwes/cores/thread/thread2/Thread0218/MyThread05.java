package com.hiwes.cores.thread.thread2.Thread0218;

/**
 * volatile关键词:使变量在多个线程间可见。
 * 反例:不使用多线程和volatile的时候，main线程一直处理while()循环，从而造成同步死循环。
 */
public class MyThread05 {
    private boolean isContinuePrint = true;

    public boolean isContinuePrint() {
        return isContinuePrint;
    }

    public void serContinuePrint(boolean isContinuePrint) {
        this.isContinuePrint = isContinuePrint;
    }

    public void printStringMethod() {
        try {
            while (isContinuePrint == true) {
                System.out.println("run printStringMethod threadName = " + Thread.currentThread().getName());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run05 {
    public static void main(String[] args) {
        MyThread05 printStringService = new MyThread05();
        printStringService.printStringMethod();
        System.out.println("我要停止它! ThreadName = " + Thread.currentThread().getName());
        printStringService.serContinuePrint(false);
    }
}