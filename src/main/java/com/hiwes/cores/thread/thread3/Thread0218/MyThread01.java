package com.hiwes.cores.thread.thread3.Thread0218;

import java.util.ArrayList;
import java.util.List;

/**
 * 不使用等待/通知机制实现线程间通信。
 * 本例使用sleep和while(true)死循环实现线程间通信。
 */
public class MyThread01 extends Thread {
    private MyList list;

    public MyThread01(MyList list) {
        super();
        this.list = list;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                list.add();
                System.out.println("添加了 " + (i + 1) + " 个元素.");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class MyThread01_2 extends Thread {
    private MyList list;

    public MyThread01_2(MyList list) {
        super();
        this.list = list;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (list.size() == 5) {
                    System.out.println("==5了，线程b要退出了!");
                    throw new InterruptedException();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyList {
    private List list = new ArrayList();

    public void add() {
        list.add("姓名");
    }

    synchronized public int size() {
        return list.size();
    }
}

class Run01 {
    public static void main(String[] args) {
        MyList list = new MyList();
        MyThread01 a = new MyThread01(list);
        a.setName("A");
        a.start();
        MyThread01_2 b = new MyThread01_2(list);
        b.setName("B");
        b.start();
    }
}