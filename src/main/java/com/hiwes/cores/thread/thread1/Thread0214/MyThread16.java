package com.hiwes.cores.thread.thread1.Thread0214;

import java.util.Random;

/**
 * 高优先级的任务总是大部分先执行完，但不代表总是先全部执行完。
 */
public class MyThread16 extends Thread {
    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        long addReult = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 50000; j++) {
                Random random = new Random();
                random.nextInt();
                addReult = addReult + j;
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("※ ※ ※ ※ ※ thread1 use time= " + (endTime - beginTime));
    }

}

class MyThread16_2 extends Thread {

    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        long addReult = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 50000; j++) {
                Random random = new Random();
                random.nextInt();
                addReult = addReult + j;
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("匚 匚 匚 匚 匚 thread2 use time= " + (endTime - beginTime));
    }
}

class Run16 {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            MyThread16 thread1 = new MyThread16();
            thread1.setPriority(Thread.MIN_PRIORITY);
            thread1.start();
            MyThread16_2 thread2 = new MyThread16_2();
            thread2.setPriority(Thread.MAX_PRIORITY);
            thread2.start();
        }
    }
}