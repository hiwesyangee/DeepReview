package com.hiwes.cores.thread.thread2.Thread0218;

/**
 * 内置类测试:
 * 有两个同步方法，但使用的是不同的锁，打印结果也是异步的。
 */
public class MyThread1 {
    static class Inner {
        public void method1() {
            synchronized ("其他的锁") {
                for (int i = 0; i <= 10; i++) {
                    System.out.println(Thread.currentThread().getName() + " i = " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public synchronized void method2() {
            for (int i = 11; i <= 20; i++) {
                System.out.println(Thread.currentThread().getName() + " i = " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}


class Run01 {
    public static void main(String[] args) {
        final MyThread1.Inner inner = new MyThread1.Inner();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                inner.method1();
            }
        }, "A");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                inner.method2();
            }
        }, "B");
        t1.start();
        t2.start();

    }
}