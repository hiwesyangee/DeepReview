package com.hiwes.cores.thread.thread4.Thread0219;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁Lock分为“公平锁”和“非公平锁”:
 * 公平锁表示:   线程获取锁的顺序是按照线程加锁的顺序来分配的，即:先来先得的FIPO先进先出顺序。
 * 非公平锁就是一个获取锁的抢占机制，随机获取锁，所以不一定是先到的先获得锁，这种方式可能导致某些线程永远获取不到锁。
 */
public class MyThread08 extends Thread {

}

class Service {
    private ReentrantLock lock;

    public Service(boolean isFair) {
        super();
        lock = new ReentrantLock(isFair);
    }

    public void serviceMethod() {
        try {
            lock.lock();
            System.out.println("ThreadName = " + Thread.currentThread().getName() + " 获得锁定.");
        } finally {
            lock.unlock();
        }
    }
}


class RunFair {
    public static void main(String[] args) {
        final Service service = new Service(true);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(" ※ 线程 " + Thread.currentThread().getName() + " 运行了.");
                service.serviceMethod();
            }
        };
        Thread[] threadArray = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threadArray[i] = new Thread(runnable);
        }

        for (int i = 0; i < 10; i++) {
            threadArray[i].start();
        }
    }
}


class RunNotFair {
    public static void main(String[] args) {
        final Service service = new Service(false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(" ※ 线程 " + Thread.currentThread().getName() + " 运行了.");
                service.serviceMethod();
            }
        };
        Thread[] threadArray = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threadArray[i] = new Thread(runnable);
        }
        for (int i = 0; i < 10; i++) {
            threadArray[i].start();
        }
    }
}

/**
 * 很奇怪的现象:
 * 当进行new ReentrantLock(false)，使用非公平锁的时候，出现了同步执行的效果.
 *
 * ※ 线程 Thread-0 运行了.
 * ThreadName = Thread-0 获得锁定.
 * ※ 线程 Thread-1 运行了.
 * ThreadName = Thread-1 获得锁定.
 * ※ 线程 Thread-2 运行了.
 * ThreadName = Thread-2 获得锁定.
 * ※ 线程 Thread-3 运行了.
 * ThreadName = Thread-3 获得锁定.
 * ※ 线程 Thread-4 运行了.
 * ThreadName = Thread-4 获得锁定.
 * ※ 线程 Thread-5 运行了.
 * ThreadName = Thread-5 获得锁定.
 * ※ 线程 Thread-6 运行了.
 * ThreadName = Thread-6 获得锁定.
 * ※ 线程 Thread-7 运行了.
 * ThreadName = Thread-7 获得锁定.
 * ※ 线程 Thread-8 运行了.
 * ThreadName = Thread-8 获得锁定.
 * ※ 线程 Thread-9 运行了.
 * ThreadName = Thread-9 获得锁定.
 */