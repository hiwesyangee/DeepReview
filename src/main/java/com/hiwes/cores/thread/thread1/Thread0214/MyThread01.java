package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 停止线程是在多线程开发时很重要的技术点，可以对线程的停止进行有效的处理。
 * 停止线程在Java语言中并不像break语句那么干脆，需要一些技巧性的处理。
 * 1.停止不了的线程====>interrupt()方法，仅仅是在当前线程中打一个停止的标记，并不是真的停止线程。
 */
public class MyThread01 extends Thread {

    @Override
    public void run() {
        super.run();
        for(int i = 0;i < 5000 ;i ++){
            System.out.println("i= "+(i+1));
        }

    }

}

class Run01{
    public static void main(String[] args) {
        try{
            MyThread01 thread = new MyThread01();
            thread.start();
            thread.sleep(2000);
            thread.interrupt();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

