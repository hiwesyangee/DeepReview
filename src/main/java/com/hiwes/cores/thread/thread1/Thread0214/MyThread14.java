package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 没有Thread.yield()的时候，CPU独占时间片；
 * 开放后，CPU将由所有任务进行抢夺，从而导致速度变慢。
 */
public class MyThread14 extends Thread {

    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < 5000000; i++) {
//             Thread.yield();  // 此时CPU独占时间片，开放后CPU将给所有任务进行抢夺，从而导致速度变慢。
            count = count + (i + 1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("用时: " + (endTime - beginTime) + " 毫秒!");
    }
}

class Run14 {
    public static void main(String[] args) {
        MyThread14 thread = new MyThread14();
        thread.start();
    }
}
