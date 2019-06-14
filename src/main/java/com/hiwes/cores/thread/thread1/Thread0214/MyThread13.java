package com.hiwes.cores.thread.thread1.Thread0214;

public class MyThread13 {

    private String username = "1";
    private String password = "11";

    public void setValue(String u, String p) {
        this.username = u;
        if (Thread.currentThread().getName().equals("a")) {
            System.out.println("停止a线程！");
            Thread.currentThread().suspend();
        }
        this.password = p;
    }

    public void printUsernamePassword() {
        System.out.println(username + "  " + password);
    }
}

class Run13 {
    public static void main(String[] args) throws InterruptedException {
        final MyThread13 thread13 = new MyThread13();
        Thread thread = new Thread() {
            @Override
            public void run() {
                thread13.setValue("a", "aa");
            }
        };
        thread.setName("a");
        thread.start();
        Thread.sleep(500);
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                thread13.printUsernamePassword();
            }
        };
        thread2.start();
    }
}
