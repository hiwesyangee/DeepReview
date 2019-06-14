package com.hiwes.cores.thread.thread1.Thread0214;

public class MyThread18 extends Thread {

    private int i = 0;

    @Override
    public void run() {
        try {
            while (true) {
                i++;
                System.out.println("i=" + (i));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run18 {
    public static void main(String[] args) {
        try {
            MyThread18 thread = new MyThread18();
            thread.setDaemon(true);
            thread.start();
            Thread.sleep(5000);
            System.out.println("我离开thread对象也不再打印了，也就是停止了。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
