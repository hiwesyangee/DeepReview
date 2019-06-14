package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * synchronized同步语法块。
 * 使用情况:当线程A长时间调用同步方法去执行某一任务的时候，线程B就需要长时间等待A释放对象锁。
 * 此时，就是用synchronized同步语法块来解决。
 * 举例:synchronized方法的弊端。 从执行结果6s上看，弊端很明显，效率很不高。两个线程都要等3s。一共6s。
 */
public class MyThread01 extends Thread {
    private Task1 task;

    public MyThread01(Task1 task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        CommonUtils.beginTime1 = System.currentTimeMillis();
        task.doLongTimeTask();
        CommonUtils.endTime1 = System.currentTimeMillis();
    }

}

class MyThread01_2 extends Thread {
    private Task1 task;

    public MyThread01_2(Task1 task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        CommonUtils.beginTime2 = System.currentTimeMillis();
        task.doLongTimeTask();
        CommonUtils.endTime2 = System.currentTimeMillis();
    }
}

class Task1 {
    private String getData1;
    private String getData2;

    public synchronized void doLongTimeTask() {
        try {
            System.out.println("begin task");
            Thread.sleep(3000);
            getData1 = "长时间处理任务后从远程返回的值1 threadName=" + Thread.currentThread().getName();
            getData2 = "长时间处理任务后从远程返回的值2 threadName=" + Thread.currentThread().getName();
            System.out.println(getData1);
            System.out.println(getData2);
            System.out.println("end task");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class CommonUtils {
    public static long beginTime1;
    public static long endTime1;
    public static long beginTime2;
    public static long endTime2;
}


class Run01 {
    public static void main(String[] args) {
        Task1 task = new Task1();
        MyThread01 thread1 = new MyThread01(task);
        thread1.start();
        MyThread01_2 thread2 = new MyThread01_2(task);
        thread2.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long beginTime = CommonUtils.beginTime1;
        if (CommonUtils.beginTime2 < CommonUtils.beginTime1) {
            beginTime = CommonUtils.beginTime2;
        }

        long endTime = CommonUtils.endTime1;
        if (CommonUtils.endTime2 > CommonUtils.endTime1) {
            endTime = CommonUtils.endTime2;
        }

        System.out.println("耗时: " + (endTime - beginTime) / 1000);
    }
}