package com.hiwes.cores.thread.thread2.Thread0215;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证:当多个线程执行带有分之判断的方法时，就会出现逻辑上的错误，有可能出现脏读。
 */
public class MyThread10 extends Thread {

    private MyOneList list;

    public MyThread10(MyOneList list) {
        super();
        this.list = list;
    }

    @Override
    public void run() {
        MyService msRef = new MyService();
        msRef.addServiceMethod(list, "A");
    }
}


class MyThread10_2 extends Thread {

    private MyOneList list;

    public MyThread10_2(MyOneList list) {
        super();
        this.list = list;
    }

    @Override
    public void run() {
        MyService msRef = new MyService();
        msRef.addServiceMethod(list, "B");
    }
}

class MyOneList {
    private List list = new ArrayList<>();

    synchronized public void add(String data) {
        list.add(data);
    }

    synchronized public int getSize() {
        return list.size();
    }
}

class MyService {
    public MyOneList addServiceMethod(MyOneList list, String data) {
        try {
            synchronized (list) {
                if (list.getSize() < 1) {
                    Thread.sleep(2000); // 模拟从远程消耗2s读取数据
                    list.add(data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }
}

class Run10 {
    public static void main(String[] args) throws InterruptedException {
        MyOneList list = new MyOneList();
        MyThread10 thread1 = new MyThread10(list);
        thread1.setName("A");
        thread1.start();
        MyThread10_2 thread2 = new MyThread10_2(list);
        thread2.setName("B");
        thread2.start();
        Thread.sleep(6000);
        System.out.println("listSize= " + list.getSize());
    }
}

/**
 * 返回结果:
 * listSize= 2
 * 脏读出现了。原因是两个线程以异步的方式返回list参数的size()大小。解决的办法就是同步化。
 */

/**
 * 修改MyService内容:
 * 由于list 参数对象在项目中是一份实例，是单例的，
 * 且正需要对list参数的getSize()方法做同步的调用，所以就对list参数进行同步处理。
 */

class MyService2 {
    public MyOneList addServiceMethod(MyOneList list, String data) {
        try {
            synchronized (list) {
                if (list.getSize() < 1) {
                    Thread.sleep(2000);
                    list.add(data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }
}