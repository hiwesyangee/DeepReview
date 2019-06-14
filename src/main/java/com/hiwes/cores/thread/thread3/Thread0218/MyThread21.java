package com.hiwes.cores.thread.thread3.Thread0218;

/**
 * 等待/通知 之 交叉备份。
 */
public class MyThread21 extends Thread {

    private DBTools dbtools;

    public MyThread21(DBTools dbtools) {
        super();
        this.dbtools = dbtools;
    }

    @Override
    public void run() {
        dbtools.backupA();
    }

}

class MyThread21_2 extends Thread {

    private DBTools dbtools;

    public MyThread21_2(DBTools dbtools) {
        super();
        this.dbtools = dbtools;
    }

    @Override
    public void run() {
        dbtools.backupB();
    }
}


class DBTools {
    volatile private boolean prevIsA = false; // 作为标记，实现数据交替备份。

    synchronized public void backupA() {
        try {
            while (prevIsA == true) {
                wait();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println("※ ※ ※ ※ ※");
            }
            prevIsA = true;
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public void backupB() {
        try {
            while (prevIsA == false) {
                wait();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println("✨ ✨ ✨ ✨ ✨");
            }
            prevIsA = false;
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run21 {
    public static void main(String[] args) {
        DBTools dbtools = new DBTools();
        for (int i = 0; i < 20; i++) {
            MyThread21 a = new MyThread21(dbtools);
            a.start();
            MyThread21_2 b = new MyThread21_2(dbtools);
            b.start();

        }
    }
}