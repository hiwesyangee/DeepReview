package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * start()的执行顺序，不是线程启动的顺序。
 */
public class MyThread03 extends Thread{

    private int i;

    public MyThread03(int i){
        super();
        this.i = i;
    }

    @Override
    public void run(){
        System.out.println(i);
    }
}


class Run03{
    public static void main(String[] args) {
        MyThread03 t1 = new MyThread03(1);
        MyThread03 t2 = new MyThread03(2);
        MyThread03 t3 = new MyThread03(3);
        MyThread03 t4 = new MyThread03(4);
        MyThread03 t5 = new MyThread03(5);
        MyThread03 t6 = new MyThread03(6);
        MyThread03 t7 = new MyThread03(7);
        MyThread03 t8 = new MyThread03(8);
        MyThread03 t9 = new MyThread03(9);
        MyThread03 t10 = new MyThread03(10);
        MyThread03 t11 = new MyThread03(11);
        MyThread03 t12 = new MyThread03(12);
        MyThread03 t13 = new MyThread03(13);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();
        t11.start();
        t12.start();
        t13.start();

    }
}
