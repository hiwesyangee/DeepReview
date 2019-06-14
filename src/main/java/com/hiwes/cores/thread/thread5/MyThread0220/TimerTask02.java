package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 2）如果执行时间早于当前时间，task任务立即执行.
 */
public class TimerTask02 extends TimerTask {
    @Override
    public void run() {
        System.out.println("任务执行了，时间为:" + new Date());
    }
}

class Run02 {
    public static void main(String[] args) {
        System.out.println("当前时间为:" + new Date());
        Calendar calendarRef = Calendar.getInstance();
        calendarRef.set(Calendar.SECOND, calendarRef.get(Calendar.SECOND) - 10);
        Date runDate = calendarRef.getTime();
        System.out.println("计划时间为:" + runDate);
        TimerTask02 task = new TimerTask02();
        Timer timer = new Timer();
        timer.schedule(task, runDate);
    }
}