package com.hiwes.cores.thread.thread2.Thread0218;

/**
 * 实验:在-server服务端运行会出现死循环。
 * 原因:在启动MyThread07线程时，变量private boolean isRunning = true;存在于公共堆栈及线程的私有堆栈中；
 * 在JVM被设置为-server模式时，为了线程运行的效率，线程一直在私有堆栈中取得isRunning的值是true。
 * 而代码thread.setRunning(false);虽然被执行，更新的却是公共堆栈中的isRunning变量值，所以一直都在死循环状态。
 *
 * 这个问题，其实是私有堆栈中的值和公共堆栈中的值不同步造成的。
 */
public class MyThread07 extends Thread {
    private boolean isRunning = true;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunniing(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        System.out.println("进入run了.");
        while (isRunning == true) {

        }
        System.out.println("被停止了.");
    }
}

class Run07 {
    public static void main(String[] args) {
        try {
            MyThread07 thread = new MyThread07();
            thread.start();
            Thread.sleep(1000);
            thread.setRunniing(false);  // 设置的是公共堆栈中的值。
            System.out.println("已经赋值为false");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}