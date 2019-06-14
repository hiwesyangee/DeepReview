package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 停止线程————异常法：在线程run()方法中直接抛出异常，跳过后续代码。
 */
public class MyThread04 extends Thread {

    @Override
    public void run() {
        super.run();
        try {
            for (int i = 0; i < 500000; i++) {
                if (this.interrupted()) {
                    System.out.println("线程已经是停止状态，马上退出!");
                    throw new InterruptedException();
                }
                System.out.println("i = " + (i + 1));
            }
            System.out.println("for()循环后面的代码不会执行!");
        } catch (InterruptedException e) {
            System.out.println("已经进入Mythread04.java类run()方法中的catch了!");
            e.printStackTrace();
        }
    }

}

class Run04 {
    public static void main(String[] args) {

        try {
            MyThread04 thread = new MyThread04();
            thread.start();
            thread.sleep(2000);
            thread.interrupt();
        } catch (InterruptedException e) {
            System.out.println("main catch");
            e.printStackTrace();
        }
    }
}
