package com.hiwes.cores.thread.thread1.Thread0214;

public class MyThread17 extends Thread {

    private int count = 0;

    public int getCOunt() {
        return count;
    }

    @Override
    public void run() {
        while (true) {
            count++;
        }
    }
}


class MyThread17_2 extends Thread {

    private int count = 0;

    public int getCount() {
        return count;

    }

    @Override
    public void run() {
        while (true) {
            count++;
        }
    }
}


class Run17 {
    public static void main(String[] args) {
        try {
            MyThread17 a = new MyThread17();
            a.setPriority(Thread.NORM_PRIORITY - 3);
            a.start();

            MyThread17_2 b = new MyThread17_2();
            b.setPriority(Thread.NORM_PRIORITY + 3);
            b.start();
            Thread.sleep(3000);
            a.stop();
            b.stop();
            System.out.println("a= " + a.getCOunt());
            System.out.println("b= " + b.getCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}