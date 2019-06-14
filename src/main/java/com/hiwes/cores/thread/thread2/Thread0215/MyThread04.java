package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 验证同步代码块的对象锁
 *
 * 不在synchronized块中的就是异步执行，在synchronized块中的就是同步执行。
 *
 * 异步执行的代码，所有线程进行抢夺资源，同步执行的代码，排队执行，一个抢到之后执行完，另一个才执行。
 */
public class MyThread04 extends Thread {

    private Task task;

    public MyThread04(Task task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        task.doLongTimeTask();
    }

}

class MyThread04_2 extends Thread {
    private Task task;

    public MyThread04_2(Task task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        task.doLongTimeTask();
    }

}

class Task {
    public void doLongTimeTask() {
        for (int i = 0; i < 100; i++) {
            System.out.println("nosynchronized threadName= " + Thread.currentThread().getName() + " i = " + (i + 1));
        }
        System.out.println(" ");
        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                System.out.println("synchronized threadName= " + Thread.currentThread().getName() + " i= " + (i + 1));
            }
        }
    }
}

class Run04 {
    public static void main(String[] args) {
        Task task = new Task();
        MyThread04 thread1 = new MyThread04(task);
        thread1.start();
        MyThread04_2 thread2 = new MyThread04_2(task);
        thread2.start();
    }
}
