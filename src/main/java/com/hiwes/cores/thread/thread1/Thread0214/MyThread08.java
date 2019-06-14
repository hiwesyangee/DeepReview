package com.hiwes.cores.thread.thread1.Thread0214;

/**
 * stop()释放锁的不良实例，外部的stop()中断了内部的等待。
 */
public class MyThread08 extends Thread {

    private SynchronizedObject object;

    public MyThread08(SynchronizedObject object) {
        super();
        this.object = object;
    }

    @Override
    public void run() {
        object.printString("b", "bb");
    }
}


class Run08 {
    public static void main(String[] args) {
        try {
            SynchronizedObject object = new SynchronizedObject();
            MyThread08 thread = new MyThread08(object);
            thread.start();
            thread.sleep(5000);
            thread.stop();
            System.out.println(object.getUsername() + "  " + object.getPassword());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SynchronizedObject {
    private String username = "a";
    private String password = "aa";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    synchronized public void printString(String username, String password) {
        System.out.println(username + "  " + password); // 传递进来的是b，bb
        try {
            this.username = username;
            Thread.sleep(100000);
            this.password = password;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}