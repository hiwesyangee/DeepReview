package com.hiwes.cores.thread.thread2.Thread0214;

/**
 * 脏读的情况。
 * 原因:方法未同步，在读写访问同时进行的时候，因为未同步，从而造成数据错误，通过synchronized解决。
 * 解决之后，在第二个synchronized方法调用之前，必须等前面的释放对象锁，才能继续执行，也就没有了脏读的基本环境。
 */
public class MyThread05 extends Thread {
    private PublicVar publicVar;

    public MyThread05(PublicVar publicVar) {
        super();
        this.publicVar = publicVar;
    }

    @Override
    public void run() {
        super.run();
        publicVar.setValue("B", "BB");
    }
}

class PublicVar {
    public String username = "A";
    public String password = "AA";

    synchronized public void setValue(String username, String password) {
        try {
            this.username = username;
            Thread.sleep(5000);
            this.password = password;
            System.out.println("setValue method thread name= " + Thread.currentThread().getName() + " username= " + username + " password= " + password);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解决脏读：synchronized。
     */
    synchronized public void getValue() {
        System.out.println("getValue method thread name= " + Thread.currentThread().getName() + " username= " + username + " password= " + password);
    }
}

class Run05 {
    public static void main(String[] args) {
        try {
            PublicVar publicVar = new PublicVar();
            MyThread05 AThread = new MyThread05(publicVar);
            AThread.start();
            Thread.sleep(1000);
            publicVar.getValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}