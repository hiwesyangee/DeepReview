package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * scheduleAtFixedRate()和schedule():
 * 都会按顺序执行，所以不需要考虑非线程安全的情况。
 * 主要区别在于有没有追赶特性。
 */
public class TimerTask11 {
}


class Run11 {
    static class MyTask extends TimerTask {

        @Override
        public void run() {
            try {
                System.out.println("begin timer = " + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println("  end timer = " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Calendar calendarRef = Calendar.getInstance();
        Date runTime = calendarRef.getTime();
        Timer timer = new Timer();
        timer.schedule(task, runTime, 3000);
    }
}


class Run11_2 {
    static class MyTask extends TimerTask {

        @Override
        public void run() {
            try {
                System.out.println("begin timer = " + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println("  end timer = " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyTask task = new MyTask();
        System.out.println("当前时间:" + new Date());
        Timer timer = new Timer();
        timer.schedule(task, 3000, 4000);
    }
}