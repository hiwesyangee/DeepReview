package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 和synchronized方法一样，synchronized(this)也是锁定当前对象的。
 */
public class MyThread06 extends Thread {

    private Task3 task;

    public MyThread06(Task3 task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        task.doLongTimeTask();
    }
}

class MyThread06_2 extends Thread {

    private Task3 task;

    public MyThread06_2(Task3 task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        task.otherMethod();
    }
}

class Task3 {
    /**
     * 验证同步锁加的是对象，而不是方法。
     */
    synchronized public void otherMethod() {
        System.out.println("----------------run---otherMethod----------------");
    }

    public void doLongTimeTask() {
        synchronized (this) {
            for (int i = 0; i < 10000; i++) {
                System.out.println("synchronized threadName = " + Thread.currentThread().getName() + " i = " + (i + 1));
            }
        }
    }
}

class Run06 {
    public static void main(String[] args) throws InterruptedException {
        Task3 task = new Task3();
        MyThread06 thread1 = new MyThread06(task);
        thread1.start();
        Thread.sleep(70);
        MyThread06_2 thread2 = new MyThread06_2(task);
        thread2.start();
    }
}