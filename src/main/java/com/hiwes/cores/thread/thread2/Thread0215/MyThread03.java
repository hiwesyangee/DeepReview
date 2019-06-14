package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * synchronized同步语法块:
 * 当一个线程访问Object的一个synchronized同步代码块时，另一个线程依然可以访问该Object对象中的非synchronized(this)同步代码块；
 * 使用这种方式，加快了执行效率，缩短了执行时间。
 * 且:同步代码块真的是同步的，真的持有当前调用对象的锁。
 */
public class MyThread03 extends Thread {
    private Task2 task;

    public MyThread03(Task2 task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        CommonUtils2.beginTime1 = System.currentTimeMillis();
        task.doLongTimeTask();
        CommonUtils2.endTime1 = System.currentTimeMillis();
    }

}

class MyThread03_2 extends Thread {
    private Task2 task;

    public MyThread03_2(Task2 task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        CommonUtils2.beginTime2 = System.currentTimeMillis();
        task.doLongTimeTask();
        CommonUtils2.endTime2 = System.currentTimeMillis();
    }
}

class Task2 {
    private String getData1;
    private String getData2;

    public void doLongTimeTask() {
        try {
            System.out.println("begin task");
            Thread.sleep(3000);

            String privateGetData1 = "长时间处理任务后从远程返回的值1 threadName=" + Thread.currentThread().getName();
            String privateGetData2 = "长时间处理任务后从远程返回的值2 threadName=" + Thread.currentThread().getName();

            synchronized (this) {
                getData1 = privateGetData1;
                getData2 = privateGetData2;
            }
            System.out.println(getData1);
            System.out.println(getData2);
            System.out.println("end task");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class CommonUtils2 {
    public static long beginTime1;
    public static long endTime1;
    public static long beginTime2;
    public static long endTime2;
}


class Run03 {
    public static void main(String[] args) {
        Task2 task = new Task2();
        MyThread03 thread1 = new MyThread03(task);
        thread1.start();
        MyThread03_2 thread2 = new MyThread03_2(task);
        thread2.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long beginTime = CommonUtils2.beginTime1;
        if (CommonUtils2.beginTime2 < CommonUtils2.beginTime1) {
            beginTime = CommonUtils2.beginTime2;
        }

        long endTime = CommonUtils2.endTime1;
        if (CommonUtils2.endTime2 > CommonUtils2.endTime1) {
            endTime = CommonUtils2.endTime2;
        }

        System.out.println(CommonUtils2.beginTime1);
        System.out.println(CommonUtils2.beginTime2);
        System.out.println(CommonUtils2.endTime1);
        System.out.println(CommonUtils2.endTime2);

        System.out.println("耗时: " + (endTime - beginTime) / 1000);
    }
}