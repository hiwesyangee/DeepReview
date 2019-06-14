package com.hiwes.cores.thread.thread3.Thread0219;

/**
 * join(long)的使用:
 * 设定当前线程Z等待调用线程X执行的时间
 */
public class MyThread03 extends Thread {

    @Override
    public void run() {
        try {
            System.out.println("begin time = " + System.currentTimeMillis());
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


class Run03 {
    public static void main(String[] args) {
        try {
            MyThread03 thread = new MyThread03();
            thread.start();
            thread.join(2000);
            // Thread.sleep(2000);
            System.out.println("  end time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}