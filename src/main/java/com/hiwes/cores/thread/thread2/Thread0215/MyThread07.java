package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 将任意对象作为对象监视器:
 * <p>
 * Java支持对“任意对象”作为“对象监视器”来实现同步的功能。大多数指的是:实例变量及方法的参数，使用格式为:synchronized(非this对象)。
 * 1.当多个线程持有(“对象监视器”为同一个对象)的前提下，
 * =======> 同一时间只有一个线程可以执行synchronized(非this对象X)同步代码块的代码。
 * 2.当持有(“对象监视器”为同一个对象)的前提下，
 * =======> 同一时间只有一个线程可以执行synchronized(非this对象X)同步代码块的代码。
 * <p>
 * <p>
 * 如果使用同步代码块锁非this对象，则synchronized(非this)代码块中的程序与同步方法是异步的，
 * 不与其他锁this同步方法抢夺this锁————————（对象都不是同一个了）。则可以大大提高效率。
 */
public class MyThread07 extends Thread {

    private Service service;

    public MyThread07(Service service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serUsernamePassword("a", "aa");
    }
}

class MyThread07_2 extends Thread {

    private Service service;

    public MyThread07_2(Service service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.serUsernamePassword("b", "bb");
    }
}

class Service {
    private String usernameParam;
    private String passwordParam;
//    private String anyThing = new String();

    public void serUsernamePassword(String username, String password) {
        try {
            String anyThing = new String();  /** 此时的anyThing在多个线程中的并不是同一个，就是异步调用。 */
            synchronized (anyThing) {
                System.out.println("线程名称: " + Thread.currentThread().getName() + " 在 " + System.currentTimeMillis() + " 进入同步块。");
                usernameParam = username;
                Thread.sleep(3000);
                passwordParam = password;
                System.out.println("线程名称: " + Thread.currentThread().getName() + " 在 " + System.currentTimeMillis() + " 离开同步块。");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run07 {
    public static void main(String[] args) {
        Service service = new Service();
        MyThread07 thread1 = new MyThread07(service);

        thread1.setName("A");
        thread1.start();

        MyThread07_2 thread2 = new MyThread07_2(service);

        thread2.setName("B");
        thread2.start();
    }

}
