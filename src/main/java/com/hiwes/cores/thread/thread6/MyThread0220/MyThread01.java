package com.hiwes.cores.thread.thread6.MyThread0220;

/**
 * 立即加载/饿汉模式:
 * 在方法调用之前，就创建了实例对象。
 */
public class MyThread01 extends Thread {

    @Override
    public void run() {
        System.out.println(MyObject01.getInstance().hashCode());
    }
}


/**
 * 立即加载方式 == 饿汉模式
 */
class MyObject01 {
    private static MyObject01 myObject = new MyObject01();

    private MyObject01() {
    }

    public static MyObject01 getInstance() {
        // 立即加载，但缺点是:不能有其他实例变量。并且因为getInstance()没有同步，可能有非线程安全问题。
        return myObject;
    }
}


class Run01{
    public static void main(String[] args) {
        MyThread01 t1 = new MyThread01();
        MyThread01 t2 = new MyThread01();
        MyThread01 t3 = new MyThread01();
        t1.start();
        t2.start();
        t3.start();
    }
}