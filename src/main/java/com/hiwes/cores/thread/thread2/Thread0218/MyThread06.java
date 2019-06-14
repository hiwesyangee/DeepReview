package com.hiwes.cores.thread.thread2.Thread0218;

/**
 * 解决同步死循环。
 * 但当此例中的代码的格式，一旦运行在-server服务器模式中64bit的JVM上时：
 * 会出现死循环。
 */
public class MyThread06 implements Runnable {

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

    @Override
    public void run() {
        printStringMethod();
    }
}


class Run06 {
    public static void main(String[] args) throws InterruptedException {
        MyThread06 printStringService = new MyThread06();
        new Thread(printStringService).start();

        Thread.sleep(5000);
        System.out.println("我要停止它! ThreadName = " + Thread.currentThread().getName());
        printStringService.serContinuePrint(false);
    }
}