package com.hiwes.cores.thread.thread6.MyThread0220;

/**
 * 静态内置类实现单例模式
 */
public class MyThread07 extends Thread {

    @Override
    public void run() {
        System.out.println(MyObject07.getInstance().hashCode());
    }
}


class MyObject07 {
    private static class MyObjectHandler {
        private static MyObject07 myObject = new MyObject07();
    }

    private MyObject07() {
    }

    public static MyObject07 getInstance() {
        return MyObjectHandler.myObject;
    }
}

class Run07{
    public static void main(String[] args) {
        MyThread07 t1 = new MyThread07();
        MyThread07 t2 = new MyThread07();
        MyThread07 t3 = new MyThread07();
        t1.start();
        t2.start();
        t3.start();
    }
}