package com.hiwes.cores.thread.thread2.Thread0214;

/**
 * 同步不能继承，还需要在子类方法中添加synchronized关键字进行同步。
 */
public class MyThread09 extends Thread {
    private Sub2 sub;

    public MyThread09(Sub2 sub) {
        super();
        this.sub = sub;
    }

    @Override
    public void run() {
        sub.serviceMethod();
    }
}

class MyThread09_2 extends Thread {
    private Sub2 sub;

    public MyThread09_2(Sub2 sub) {
        super();
        this.sub = sub;
    }

    @Override
    public void run() {
        sub.serviceMethod();
    }
}

class Main2 {
    synchronized public void serviceMethod() {
        try {
            System.out.println("int main 下一步 sleep begin threadName= " + Thread.currentThread().getName() + " time= " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("int main 下一步 sleep end threadName= " + Thread.currentThread().getName() + " time =" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Sub2 extends Main2 {

    @Override
    synchronized public void serviceMethod() {
        try {
            System.out.println("int sub 下一步 sleep begin threadName= " + Thread.currentThread().getName() + " time= " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("int sub 下一步 sleep end threadName= " + Thread.currentThread().getName() + " time= " + System.currentTimeMillis());
            super.serviceMethod();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run09 {
    public static void main(String[] args) {
        Sub2 sub = new Sub2();
        MyThread09 a = new MyThread09(sub);
        a.setName("A");
        a.start();
        MyThread09_2 b = new MyThread09_2(sub);
        b.setName("B");
        b.start();
    }
}