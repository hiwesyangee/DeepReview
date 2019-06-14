package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * setPriority(int)修改优先级，优先级可以被继承。
 */
public class MyThread15 extends Thread {

    @Override
    public void run() {
        System.out.println("MyThread run priority= " + this.getPriority());
        MyThread15_2 thread2 = new MyThread15_2();
        thread2.start();
    }
}

class MyThread15_2 extends Thread {

    @Override
    public void run() {
        System.out.println("MyThread2 run priority= " + this.getPriority());
    }
}

class Run15 {
    public static void main(String[] args) {
        System.out.println("main thread begin priority= " + Thread.currentThread().getPriority());
        Thread.currentThread().setPriority(6); // 这个时候，Thread已经修改了优先级为6。
        System.out.println("main thread end priority= " + Thread.currentThread().getPriority());
        MyThread15 thread1 = new MyThread15();
        thread1.start();
    }
}