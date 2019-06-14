package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer中允许多个TimerTask任务及延时的测试.
 */
public class TimerTask03 extends TimerTask {
    @Override
    public void run() {
        System.out.println("任务执行了，时间为:" + new Date());
    }
}

class Run03 {
    public static void main(String[] args) {
        System.out.println("当前时间为:" + new Date());
        Calendar calendarRef1 = Calendar.getInstance();
        calendarRef1.add(Calendar.SECOND, 10);

        Date runDate1 = calendarRef1.getTime();
        System.out.println("计划任务1时间为:" + runDate1);

        Calendar calendarRef2 = Calendar.getInstance();
        calendarRef2.add(Calendar.SECOND, 5);
        Date runDate2 = calendarRef2.getTime();
        System.out.println("计划任务2时间为:" + runDate2);

        TimerTask02 task1 = new TimerTask02();
        TimerTask02 task2 = new TimerTask02();

        Timer timer = new Timer();
        timer.schedule(task1, runDate1);
        timer.schedule(task2, runDate2);

    }
}