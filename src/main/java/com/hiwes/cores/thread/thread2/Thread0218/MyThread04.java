package com.hiwes.cores.thread.thread2.Thread0218;

/**
 * 只要对象不变，哪怕对象的值改变，运行结果依然是同步的。
 */
public class MyThread04 extends Thread {
    private MyService2 service;
    private Userinfo userinfo;

    public MyThread04(MyService2 service, Userinfo userinfo) {
        super();
        this.service = service;
        this.userinfo = userinfo;
    }

    @Override
    public void run() {
        service.serviceMethodA(userinfo);
    }
}

class MyThread04_2 extends Thread {
    private MyService2 service;
    private Userinfo userinfo;

    public MyThread04_2(MyService2 service, Userinfo userinfo) {
        super();
        this.service = service;
        this.userinfo = userinfo;
    }

    @Override
    public void run(){
        service.serviceMethodA(userinfo);
    }
}

class Userinfo {
    private String username;
    private String password;

    public Userinfo() {
        username = "0";
        password = "0";
    }

    public Userinfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

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
}

class MyService2 {
    public void serviceMethodA(Userinfo userinfo) {
        synchronized (userinfo) {
            try {
                System.out.println(Thread.currentThread().getName());
                userinfo.setUsername("abcabcabc");
                Thread.sleep(3000);
                System.out.println("end! time = " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Run04 {
    public static void main(String[] args) {
        try {
            MyService2 service = new MyService2();
            Userinfo userinfo = new Userinfo();
            MyThread04 a = new MyThread04(service, userinfo);
            a.setName("A");
            a.start();
            Thread.sleep(50);
            MyThread04_2 b = new MyThread04_2(service, userinfo);
            b.setName("B");
            b.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
