package com.hiwes.cores.thread.thread3.Thread0218;

import java.util.ArrayList;
import java.util.List;

/**
 * 注意:notify并不会立即释放对象锁，所以wait也不会立即执行。
 * 关键字synchronized可以将任何一个Object对象作为同步对象来看待，而Java为每个Object都实现了wait()方法和notify()方法，
 * 它们必须用在被synchronized同步的Object的临界区内。
 */
public class MyThread04 extends Thread {
    private Object lock;

    public MyThread04(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                if (MyList4.size() != 5) {
                    System.out.println("wait begin " + System.currentTimeMillis());
                    lock.wait();  // 此时下面的代码不会运行。
                    System.out.println("wait end " + System.currentTimeMillis());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class MyThread04_2 extends Thread {
    private Object lock;

    public MyThread04_2(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    MyList4.add();
                    if (MyList4.size() == 5) {
                        lock.notify();  // 此时运行了，但是并不是立即释放锁，所以上面的wait会最后输出。
                        System.out.println("已发出通知.");  // 当5的时候发出通知，唤醒之前的wait()。
                    }
                    System.out.println("添加了 " + (i + 1) + "个元素");
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class MyList4 {
    private static List list = new ArrayList();

    public static void add() {
        list.add("姓名");
    }

    public static int size() {
        return list.size();
    }
}


class Run04 {
    public static void main(String[] args) {
        try {
            Object lock = new Object();
            MyThread04 a = new MyThread04(lock);
            a.start();
            Thread.sleep(50);
            MyThread04_2 b = new MyThread04_2(lock);
            b.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}