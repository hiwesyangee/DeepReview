package com.hiwes.cores.thread.thread2.Thread0215;

import java.util.ArrayList;
import java.util.List;

/**
 * 由于线程执行方法的顺序不确定，所以当AB两个线程执行带有分之判断的方法时，就会出现逻辑上的错误，有可能出现脏读。
 */
public class MyThread09 extends Thread {

    private MyList list;

    public MyThread09(MyList list) {
        super();
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            list.add("threadA" + (i + 1));
        }
    }
}


class MyThread09_2 extends Thread {

    private MyList list;

    public MyThread09_2(MyList list) {
        super();
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            list.add("threadB" + (i + 1));
        }
    }
}

class MyList {
    private List list = new ArrayList<>();

    synchronized public void add(String username) {
        System.out.println("ThreadName= " + Thread.currentThread().getName() + " 执行了add方法!");
        list.add(username);
        System.out.println("ThreadName= " + Thread.currentThread().getName() + " 退出了add方法!");
    }

    synchronized public int getSize() {
        System.out.println("ThreadName= " + Thread.currentThread().getName() + " 执行了getSize方法!");
        int sizeValue = list.size();
        System.out.println("ThreadName= " + Thread.currentThread().getName() + " 退出了getSize方法!");
        return sizeValue;
    }

}


class Run09 {
    public static void main(String[] args) {
        MyList list = new MyList();
        MyThread09 thread1 = new MyThread09(list);
        thread1.setName("A");
        thread1.start();
        MyThread09_2 thread2 = new MyThread09_2(list);
        thread2.setName("B");
        thread2.start();
    }
}