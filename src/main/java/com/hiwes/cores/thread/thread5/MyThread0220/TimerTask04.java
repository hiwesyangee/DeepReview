package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TimerTask是以队列的方式一个一个被顺序性地执行，所以执行的时间有可能和预期的时间不一样，
 * 因为前面的任务有可能消耗的时间较长，则后面的任务运行的时间也被延后了。
 * 原因:task是放入队列中的，需要一个一个运行.
 */
public class TimerTask04 extends TimerTask {
    @Override
    public void run() {
        try {
            System.out.println("A begin timer = " + new Date());
            Thread.sleep(20000);
            System.out.println("A   end timer = " + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class TimerTask04_2 extends TimerTask {

    @Override
    public void run() {
        System.out.println("B begin timer = " + new Date());
        System.out.println("B   end timer = " + new Date());
    }
}

class Run04 {
    public static void main(String[] args) {
        System.out.println("当前时间为:" + new Date());
        Calendar calendarRef1 = Calendar.getInstance();
        Date runTime1 = calendarRef1.getTime();
        System.out.println("A 计划时间为:" + runTime1);

        Calendar calendarRef2 = Calendar.getInstance();
        calendarRef2.add(Calendar.SECOND, 10);
        Date runTime2 = calendarRef2.getTime();
        System.out.println("B 计划时间为:" + runTime2);

        TimerTask04 task1 = new TimerTask04();
        TimerTask04_2 task2 = new TimerTask04_2();

        Timer timer = new Timer();

        timer.schedule(task1, runTime1);
        timer.schedule(task2, runTime2);
    }
}