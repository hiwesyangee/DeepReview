package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 在for()循环中，判断线程是否是停止状态，如果是，就不再执行后续代码。
 * 停止线程————异常法1：还没使用异常法的代码，在for()循环之后的代码依然执行。
 */
public class MyThread03 extends Thread {

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 500000; i++) {
            if (Thread.interrupted()) {
                System.out.println("线程已经是停止状态，马上退出!");
                break;
            }
            System.out.println("i = " + (i + 1));
        }

        System.out.println("for()循环后面的不会停止!");

    }

}

class Run03 {
    public static void main(String[] args) {
        try{
            MyThread03 thread= new MyThread03();
            thread.start();
            thread.sleep(2000);
            thread.interrupt();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("end!");
    }
}
