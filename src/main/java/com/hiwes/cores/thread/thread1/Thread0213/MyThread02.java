package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * 使用随机数的形式使线程得到挂起的效果，从而表现出CPU执行哪个线程具有不确定性。
 */
public class MyThread02 extends Thread{

    @Override
    public void run(){
        try{
            for(int i = 0 ; i < 10; i++){
                int time = (int)(Math.random()*1000);
                Thread.sleep(time);
                System.out.println("run= "+Thread.currentThread().getName());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

class Run02{
    public static void main(String[] args) {
        try{
            MyThread02 thread = new MyThread02();
            thread.setName("myThread");
            thread.start();
            for(int i =0;i<10;i++){
                int time = (int)(Math.random()*1000);
                Thread.sleep(time);
                System.out.println("main= " +Thread.currentThread().getName());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
