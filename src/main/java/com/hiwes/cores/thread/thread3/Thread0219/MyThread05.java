package com.hiwes.cores.thread.thread3.Thread0219;

/**
 * 延伸性陷阱解释:
 * join(long)大部分情况下先抢到资源，但是也有没有抢到资源的情况。
 * 1）b.join(2000)方法先抢到b锁，然后将锁进行释放；
 * 2）ThreadA抢到锁，打印ThreadA begin，然后sleep(5000);
 * 3）ThreadA打印ThreadA end，并释放锁；
 * 4）（此时）join(2000)和ThreadB抢夺锁，如果join抢到，发现已经时间过了，所以释放锁，打印main end；
 *          如果ThreadB抢到锁，打印ThreadB begin和ThreadB end；
 * 5）最后再是没有抢到资源的线程进行打印。
 */
public class MyThread05 extends Thread {
    private MyThread05_2 b;

    public MyThread05(MyThread05_2 b) {
        super();
        this.b = b;
    }

    @Override
    public synchronized void start() {
        try {
            synchronized (b) {
                System.out.println("begin A ThreadName = " + Thread.currentThread().getName() + "   " + System.currentTimeMillis());
                Thread.sleep(5000);
                System.out.println("  end A ThreadName = " + Thread.currentThread().getName() + "   " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread05_2 extends Thread {
    @Override
    public void run() {
        try {
            System.out.println("begin B ThreadName = " + Thread.currentThread().getName() + "   " + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("  end B ThreadName = " + Thread.currentThread().getName() + "   " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


class Run05 {
    public static void main(String[] args) {
        try {
            MyThread05_2 b = new MyThread05_2();
            MyThread05 a = new MyThread05(b);
            a.start();
            b.start();
            b.join(2000);
            System.out.println("        main end " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class RunFirst{
    public static void main(String[] args) {
        MyThread05_2 b = new MyThread05_2();
        MyThread05 a=  new MyThread05(b);
        a.start();
        b.start();
        System.out.println("        main end " + System.currentTimeMillis());
    }
}