package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 死锁:
 * 不同线程都在等待不可能释放的锁，从而导致所有任务都不能继续完成。
 *
 * 死锁，是程序设计的BUG，在设计程序时就要避免双方互相持有对方的锁的情况。
 * 本例中使用synchronized嵌套的代码结构来实现死锁，但是不使用也会出现死锁，与嵌套与否无关。
 *
 * 只要互相等待对方释放锁就有可能出现死锁。
 */
public class MyThread20 implements Runnable {

    public String username;
    public Object lock1 = new Object();
    public Object lock2 = new Object();

    public void setFalg(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        if (username.equals("a")) {
            synchronized (lock1) {
                try {
                    System.out.println("username = " + username);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println("按 lock1 -> lock2 代码顺序执行了!");
                }
            }
//            synchronized (lock2) {
//                System.out.println("按 lock1 -> lock2 代码顺序执行了!");
//            }
        }
        if (username.equals("b")) {
            synchronized (lock2) {
                try {
                    System.out.println("username = " + username);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println("按 lock2 -> lock1 代码顺序执行了!");
                }
            }
        }
    }
}

class Run20 {
    public static void main(String[] args) {
        try {
            MyThread20 t1 = new MyThread20();
            t1.setFalg("a");
            Thread thread1 = new Thread(t1);
            thread1.start();
            Thread.sleep(100);
            t1.setFalg("b");
            Thread thread2 = new Thread(t1);
            thread2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
