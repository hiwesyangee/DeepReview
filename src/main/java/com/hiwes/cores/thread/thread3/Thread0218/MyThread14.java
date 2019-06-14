package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * 假死。线程进入Waitting等待状态，如果所有线程都进入等待状态，程序就不再执行任何业务功能。
 * 出现假死的主要原因:有可能是因为连续唤醒同类。
 */
public class MyThread14 extends Thread {
    private P14 p;

    public MyThread14(P14 p) {
        super();
        this.p = p;
    }

    @Override
    public void run() {
        while (true) {
            p.setValue();
        }
    }
}

class MyThread14_2 extends Thread {
    private C14 c;

    public MyThread14_2(C14 c) {
        super();
        this.c = c;
    }

    @Override
    public void run() {
        c.getValue();
    }
}


class P14 {
    private String lock;

    public P14(String lock) {
        super();
        this.lock = lock;
    }

    public void setValue() {
        try {
            synchronized (lock) {
                while (!ValueObject14.value.equals("")) {
                    System.out.println("生产者 " + Thread.currentThread().getName() + " waiting了。※");
                    lock.wait();
                }
                System.out.println("生产者 " + Thread.currentThread().getName() + " Runnable了。");
                String value = System.currentTimeMillis() + "_" + System.nanoTime();
                ValueObject14.value = value;
//                lock.notify();
                lock.notifyAll();  // 不止唤醒同类，异类也会唤醒。
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class C14 {
    private String lock;

    public C14(String lock) {
        super();
        this.lock = lock;
    }

    public void getValue() {
        try {
            synchronized (lock) {
                while (ValueObject14.value.equals("")) {
                    System.out.println("消费者 " + Thread.currentThread().getName() + " wating了.✨");
                    lock.wait();
                }
                System.out.println("消费者 " + Thread.currentThread().getName() + " Runnable了.");
                ValueObject14.value = "";
//                lock.notify();
                lock.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class ValueObject14 {
    public static String value = "";
}


class Run14 {
    public static void main(String[] args) throws InterruptedException {
        String lock = new String("");
        P14 p = new P14(lock);
        C14 c = new C14(lock);

        MyThread14[] pThread = new MyThread14[2];
        MyThread14_2[] cThread = new MyThread14_2[2];

        for (int i = 0; i < 2; i++) {
            pThread[i] = new MyThread14(p);
            pThread[i].setName("生产者 " + (i + 1));
            cThread[i] = new MyThread14_2(c);
            cThread[i].setName("消费者 " + (i + 1));
            pThread[i].start();
            cThread[i].start();
        }

        Thread.sleep(5000);

        Thread[] threadArray = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
        Thread.currentThread().getThreadGroup().enumerate(threadArray);
        for (int i = 0; i < threadArray.length; i++) {
            System.out.println(threadArray[i].getName() + " " + threadArray[i].getState());
        }
    }
}