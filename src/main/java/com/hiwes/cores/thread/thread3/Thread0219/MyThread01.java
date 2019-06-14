package com.hiwes.cores.thread.thread3.Thread0219;

/**
 * 使用join解决主线程等待子线程执行完毕之后结束。
 * 使所属的线程对象X正常执行run()方法中的任务，而使当前线程Z进行无限期的阻塞，等待线程X销毁后在执行线程Z后面的代码。
 */
public class MyThread01 extends Thread{
    @Override
    public void run() {
        try {
            int secondValue = (int) (Math.random() * 10000);
            System.out.println(secondValue);
            Thread.sleep(secondValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run01{
    public static void main(String[] args) {
        try{
            MyThread01 thread = new MyThread01();
            thread.start();
            thread.join();  // 做到线程阻塞。
            System.out.println("我想当thread对象执行完毕之后我再执行，我做到了.");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}