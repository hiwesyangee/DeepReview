package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * schedule()和scheduleAtFixedRate()的区别:
 */
public class TimerTask12 {
    static class MyTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("begin timer = " + new Date());
            System.out.println("  end timer = " + new Date());
        }
    }

    public static void main(String[] args) {
        MyTask task = new MyTask();
        System.out.println("现在执行时间: " + new Date());
        Calendar calendarRef = Calendar.getInstance();
        calendarRef.set(Calendar.SECOND, calendarRef.get(Calendar.SECOND) - 20);
        Date runTime = calendarRef.getTime();
        System.out.println("计划执行时间: " + runTime);
        Timer timer = new Timer();
        timer.schedule(task, runTime, 2000);
    }
}


class TimerTask12_2 {
    static class MyTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("begin timer = " + new Date());
            System.out.println("  end timer = " + new Date());
        }
    }

    public static void main(String[] args) {
        MyTask task = new MyTask();
        System.out.println("现在执行时间: " + new Date());
        Calendar calendarRef = Calendar.getInstance();
        calendarRef.set(Calendar.SECOND, calendarRef.get(Calendar.SECOND) - 20);
        Date runTime = calendarRef.getTime();
        System.out.println("计划执行时间: " + runTime);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, runTime, 2000);
    }
}