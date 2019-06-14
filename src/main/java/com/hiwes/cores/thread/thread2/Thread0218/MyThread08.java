package com.hiwes.cores.thread.thread2.Thread0218;

/**
 * 使用volatile关键字，在线程访问isRunning这个变量时，强制性从公共对战进行取值。
 * 注意：强制！！！藏家乐实例变量在多个线程之间的可见性。但它的缺点是:不支持原子性。
 */
public class MyThread08 extends Thread {
    volatile private boolean isRunning = true;

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

class Run08 {
    public static void main(String[] args) {
        try {
            MyThread08 thread = new MyThread08();
            thread.start();
            Thread.sleep(1000);
            thread.setRunniing(false);  // 设置的是公共堆栈中的值。
            System.out.println("已经赋值为false");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
