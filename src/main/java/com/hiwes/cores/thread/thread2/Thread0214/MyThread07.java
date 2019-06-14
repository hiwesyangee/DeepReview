package com.hiwes.cores.thread.thread2.Thread0214;

/**
 * 可重入锁也支持在父子类继承的环境中。
 */
public class MyThread07 extends Thread {

    @Override
    public void run() {
        Sub sub = new Sub();
        sub.operateISubMethod();
    }
}

class Main {
    public int i = 10; // 子类无条件继承公共成员。

    synchronized public void operateIMainMethod() {
        try {
            i--;
            System.out.println("main print i = " + i);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Sub extends Main {
    synchronized public void operateISubMethod() {
        try {
            while (i > 0) {
                i--;
                System.out.println("sub print i = " + i);
                Thread.sleep(500);
                this.operateIMainMethod();  // 通过“可重入锁”,调用父类的同步方法:operateIMainMethod()。
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run07 {
    public static void main(String[] args) {
        MyThread07 thread = new MyThread07();
        thread.start();
    }
}