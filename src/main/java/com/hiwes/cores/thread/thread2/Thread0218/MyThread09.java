package com.hiwes.cores.thread.thread2.Thread0218;

/**
 * volatile不支持同步性————不支持原子性的验证。
 */
public class MyThread09 extends Thread {
    volatile public static int count;

    private static void addCount() {
        for (int i = 0; i < 100; i++) {
            count++;
        }
        System.out.println("count = " + count);
    }

    @Override
    public void run() {
        addCount();
    }
}


class Run09 {
    public static void main(String[] args) {
        MyThread09[] myThreadArray = new MyThread09[100];
        for (int i = 0; i < 100; i++) {
            myThreadArray[i] = new MyThread09();
        }
        for (int i = 0; i < 100; i++) {
            myThreadArray[i].start();
        }
    }
}


class MyThread09_2 extends Thread{
    volatile public static int count;
    // 注意:一定要加static，这样synchronized与static锁的内容就是class了，达到同步
    synchronized private static void addCount(){
        for(int i = 0 ; i < 100 ; i++){
            count++;
        }
        System.out.println("count = "+count);
    }

    @Override
    public void run() {
        addCount();
    }
}

class Run09_2{
    public static void main(String[] args) {
        MyThread09_2[] myThreadArray = new MyThread09_2[100];
        for (int i = 0; i < 100; i++) {
            myThreadArray[i] = new MyThread09_2();
        }
        for (int i = 0; i < 100; i++) {
            myThreadArray[i].start();
        }
    }
}