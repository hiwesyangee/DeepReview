package com.hiwes.cores.thread.thread6.MyThread0220;

/**
 * 静态代码块中的代码，在使用类的时候就已经执行了，所以可以应用静态代码块这个特性来实现单例模式
 */
public class MyThread09 extends Thread {
    @Override
    public void run() {
        System.out.println(MyObject09.getInstance().hashCode());
    }
}

class MyObject09 {
    private static MyObject09 instance = null;

    private MyObject09() {
    }

    static {
        instance = new MyObject09();
    }

    public static MyObject09 getInstance() {
        return instance;
    }
}


class Run09{
    public static void main(String[] args) {
        MyThread09 t1 = new MyThread09();
        MyThread09 t2 = new MyThread09();
        MyThread09 t3 = new MyThread09();
        t1.start();
        t2.start();
        t3.start();
    }
}