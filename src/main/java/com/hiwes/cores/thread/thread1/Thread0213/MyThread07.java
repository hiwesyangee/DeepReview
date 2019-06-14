package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * 使用synchronized关键字，解决非线程安全问题。
 */
public class MyThread07 {
    private static String usernameRef;
    private static String passwordRef;

    synchronized public static void doPost(String username, String password) {
        try {
            usernameRef = username;
            if (username.equals("a")) {
                Thread.sleep(5000);
            }
            passwordRef = password;
            System.out.println("username=" + usernameRef + " password=" + password);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Alogin extends Thread {
    @Override
    public void run() {
        MyThread07.doPost("a", "aa");
    }
}

class BLogin extends Thread {
    @Override
    public void run() {
        MyThread07.doPost("b", "bb");
    }
}

class Run07 {
    public static void main(String[] args) {
        Alogin a = new Alogin();
        a.start();
        BLogin b = new BLogin();
        b.start();
    }
}