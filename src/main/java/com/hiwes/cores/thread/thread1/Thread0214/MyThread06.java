package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 停止线程————sleep中被停止。
 * 先中断，再sleep。
 */
public class MyThread06 extends Thread {

    @Override
    public void run(){
        try{
            for(int i = 0 ; i < 100000; i ++){
                System.out.println("i = "+(i+1));
            }
            System.out.println("run begin");
            Thread.sleep(200000);  // 沉睡中停止。
            System.out.println("run end");
        }catch (InterruptedException e){
            System.out.println("先停止，再遇到sleep！进入catch。isInterrupted: " + this.isInterrupted());
            e.printStackTrace();
        }
    }
}

class Run06{
    public static void main(String[] args) {
        MyThread06 thread = new MyThread06();
        thread.start();
        thread.interrupt(); // 先中断，再sleep。
        System.out.println("end!");
    }
}