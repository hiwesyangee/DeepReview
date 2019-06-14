package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 使用已经被弃用的suspend()和resume()方法，实现线程的阻塞。
 */
public class MyThread10 extends Thread {

    private long i = 0;

    public Long getI() {
        return i;
    }

    public void serI(long i) {
        this.i = i;
    }

    @Override
    public void run() {
        while (true) {
            i++;
        }
    }

}


class Run10 {
    public static void main(String[] args) {
        try {
            MyThread10 thread = new MyThread10();
            thread.start();
            Thread.sleep(5000);
            // A段
            thread.suspend();
            System.out.println("A= " + System.currentTimeMillis() + " i= " + thread.getI());
            Thread.sleep(5000);
            System.out.println("A= " + System.currentTimeMillis() + " i= " + thread.getI());

            // B段
            thread.resume();
            Thread.sleep(5000);

            // C段
//            thread.suspend(); // 一旦这里不阻塞，i的值就会继续增大
            System.out.println("B= " + System.currentTimeMillis() + " i= " + thread.getI());
            Thread.sleep(5000);
            System.out.println("B= " + System.currentTimeMillis() + " i= " + thread.getI());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


