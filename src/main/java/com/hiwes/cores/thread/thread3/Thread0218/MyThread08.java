package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * 使用notify(),只能随机唤醒wait状态中的一个线程，具有随机性;
 * 多次调用此方法，会唤醒所有的waiting状态线程，多余的代码无效。
 * 使用notifyAll()，唤醒所有wait状态的线程。
 */
public class MyThread08 extends Thread {
    private Object lock;

    public MyThread08(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service8 service = new Service8();
        service.testMethod(lock);
    }
}


class MyThread08_2 extends Thread {
    private Object lock;

    public MyThread08_2(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service8 service = new Service8();
        service.testMethod(lock);
    }
}


class MyThread08_3 extends Thread {
    private Object lock;

    public MyThread08_3(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        Service8 service = new Service8();
        service.testMethod(lock);
    }
}


class Service8 {
    public void testMethod(Object lock) {
        try {
            synchronized (lock) {
                System.out.println("begin wait ThreadName = " + Thread.currentThread().getName());
                lock.wait();
                System.out.println("  end wait ThreadName = " + Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class NotifyThread extends Thread {

    private Object lock;

    public NotifyThread(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            lock.notify();
            /**
             * 多次调用会唤醒全部waiting状态的线程。
             */
            lock.notify();
            lock.notify();
            lock.notify();
            lock.notify();
            lock.notify();
            lock.notify();
            /**
             * 当没有线程需要被唤醒的时候，调用notify()时，代码无效。
             */
        }
    }
}


class Run08 {
    public static void main(String[] args) throws InterruptedException {
        Service8 service = new Service8();
        MyThread08 a = new MyThread08(service);
        a.start();
        MyThread08_2 b = new MyThread08_2(service);
        b.start();
        MyThread08_3 c = new MyThread08_3(service);
        c.start();
        Thread.sleep(1000);
        NotifyThread notifyThread = new NotifyThread(service);
        notifyThread.start();
    }
}
/**
 * 打印结果1:
 * begin wait ThreadName = Thread-0
 * begin wait ThreadName = Thread-1
 * begin wait ThreadName = Thread-2
 * end wait ThreadName = Thread-0
 * 为什么只结束了一个线程呢？
 * 原因:使用notify()方法，只能随机唤醒wait状态的线程中的其中一个，所以只结束一个，并且有随机性。
 */


/**
 * 打印结果2:
 * begin wait ThreadName = Thread-0
 * begin wait ThreadName = Thread-1
 * begin wait ThreadName = Thread-2
 *   end wait ThreadName = Thread-0
 *   end wait ThreadName = Thread-2
 *   end wait ThreadName = Thread-1
 */


class NotifyThread2 extends Thread {
    private Object lock;

    public NotifyThread2(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            lock.notifyAll();  // 调用notifyAll(),唤醒所有waiting状态线程。
        }
    }
}

class Run08_2 {
    public static void main(String[] args) throws InterruptedException {
        Service8 service = new Service8();
        MyThread08 a = new MyThread08(service);
        a.start();
        MyThread08_2 b = new MyThread08_2(service);
        b.start();
        MyThread08_3 c = new MyThread08_3(service);
        c.start();
        Thread.sleep(1000);
        NotifyThread2 notifyThread = new NotifyThread2(service);
        notifyThread.start();
    }
}