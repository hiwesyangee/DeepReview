package com.hiwes.cores.thread.thread1.Thread0213;

public class Test01 {
    public static void main(String[] args) {
        Thread thread = new ThreadDemo();
        thread.run();
    }
}


class ThreadTest implements Runnable {
    @Override
    public void run() {
        System.out.println("nihao!");
    }
}

class ThreadDemo extends Thread {
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            Thread thread = new Thread(new ThreadTest());
            thread.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
