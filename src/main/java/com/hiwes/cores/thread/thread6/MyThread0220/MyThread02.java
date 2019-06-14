package com.hiwes.cores.thread.thread6.MyThread0220;

/**
 * 延迟加载/懒汉模式:
 * 在调用get()方法的时候，才创建实例。（常见实现方法是在get()中进行new实例化）
 */
public class MyThread02 extends Thread{

    @Override
    public void run(){
        System.out.println(MyObject02.getInstance().hashCode());
    }
}


class MyObject02 {
    private static MyObject02 myObject;

    private MyObject02() {
    }

    public static MyObject02 getInstance() {
        // 延迟加载
        if (myObject != null) {
            return myObject;
        } else {
            return new MyObject02();
        }
    }
}

class Run02{
    public static void main(String[] args) {
        MyThread02 t1 = new MyThread02();
        MyThread02 t2 = new MyThread02();
        t1.start();
        t2.start();  // 对象不同，会出现多个实例，与单例模式的初衷相违背。
    }
}