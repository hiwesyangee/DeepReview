package com.hiwes.cores.thread.thread3.Thread0219;

/**
 * join。
 * 在很多情况下， 主线程创建并启动子线程，如果子线程要进行大量的耗时运算，主线程往往早于子线程结束前结束。
 * 这时，如果主线程需要等待子线程执行结束之后再结束，如:(子线程计算一个值，主线程需要用到.)
 * 就需要用到join()方法。
 * 作用:等待线程对象销毁。
 * 本例:使用join之前的铺垫。
 */
public class MyThreadTest extends Thread {

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


class Run {
    public static void main(String[] args) {
        MyThreadTest thread = new MyThreadTest();
        thread.start();

        // Thread.sleep(?);
        System.out.println("我想当thread对象执行完毕之后我再执行.");
        System.out.println("但上面代码的sleep()的值应该是多少呢.");
        System.out.println("答案是: 完全不能肯定.");
    }
}

/**
 * 随机计算值，每次结果不一样。
 */