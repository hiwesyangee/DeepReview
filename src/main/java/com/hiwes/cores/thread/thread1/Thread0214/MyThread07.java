package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * 停止线程————暴力停止：stop()。
 * 此种方式停止，非常暴力，非常不建议这样使用。停止线程会导致它解锁所有已锁定的监视器。
 * （当ThreadDeath异常在堆栈中传播时，监视器被解锁。）
 * 如果之前由这些监视器保护的对象中的任何一个处于不一致状态，则其他线程现在可以以不一致的状态查看这些对象。
 * 当线程操作受损对象时，可能导致任意行为。这种行为可能微妙且难以检测，或者可能会发音。
 * 与其他未经检查的异常不同，可以 ThreadDeath静默地杀死线程; 因此，用户没有警告他的程序可能被损坏。
 * 腐败现象可能会在实际损害发生后随时出现，甚至可能在未来数小时甚至数天。
 */
public class MyThread07 extends Thread {

    private int i = 0;

    @Override
    public void run() {
        try {
            while (true) {
                i++;
                System.out.println("i = " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run07 {
    public static void main(String[] args) {
        try {
            MyThread07 thread = new MyThread07();
            thread.start();
            Thread.sleep(8000);
            thread.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ThreadDeath extends Thread{

    @Override
    public void run() {
        try{
            this.stop();
        }catch (java.lang.ThreadDeath e){
            System.out.println("进入catch()方法了。");
            e.printStackTrace();
        }
    }
}

class ThreadDeathTest{
    public static void main(String[] args) {
        ThreadDeath thread = new ThreadDeath();
        thread.start();
    }
}
