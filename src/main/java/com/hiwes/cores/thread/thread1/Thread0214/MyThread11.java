package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * suspend和resume的缺点：独占。
 * 使用不当，会造成公共的同步对象的独占，是的其他线程无法访问公共同步对象
 */
public class MyThread11 extends Thread {
}

class Run11 {
    public static void main(String[] args) {
        try{
            final SynchronizedObject2 object= new SynchronizedObject2();
            Thread thread = new Thread(){
              @Override
              public void run(){
                  object.printString();
              }
            };
            thread.setName("a");
            thread.start();
            Thread.sleep(1000);
            Thread thread2 = new Thread(){
              @Override
              public void run(){
                  System.out.println("thread2启动了，但进入不了printString()方法!只打印一个begin");
                  System.out.println("因为printString()方法被a线程锁定并且永远suspend暂停了！");
                  object.printString();
              }
            };
            thread2.start();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

class SynchronizedObject2 {
    synchronized public void printString() {
        System.out.println("begin.");
        if (Thread.currentThread().getName().equals("a")) {
            System.out.println("a线程永远 suspend了！");
            Thread.currentThread().suspend();
        }
        System.out.println("end.");
    }
}
