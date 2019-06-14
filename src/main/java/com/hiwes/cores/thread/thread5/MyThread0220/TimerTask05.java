package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * schedule(TimerTask task, Date firstTime, long delay).
 * 在指定的日期，按执行的间隔周期，无限循环执行某一任务。
 * 1）如果计划时间晚于当前时间——————在未来执行。
 * 2）如果计划时间早于当前时间——————立即执行。
 */
public class TimerTask05 extends TimerTask {

    @Override
    public void run() {
        System.out.println("任务执行了，时间为: " + new Date());
    }
}


class Run05 {
    public static void main(String[] args) {
        System.out.println("当前时间为: " + new Date());
        Calendar calendarRef = Calendar.getInstance();
        calendarRef.add(Calendar.SECOND, 10);
        Date runTime = calendarRef.getTime();
        System.out.println("计划时间为: " + runTime);
        TimerTask05 task = new TimerTask05();
        Timer timer = new Timer();
        timer.schedule(task, runTime, 2000);  // 任务，起始时间，循环间隔时间
    }
}


class Run05_2 {
    public static void main(String[] args) {
        System.out.println("当前时间为: " + new Date());
        Calendar calendarRef = Calendar.getInstance();
        calendarRef.set(Calendar.SECOND, calendarRef.get(Calendar.SECOND) - 10);
        Date runTime = calendarRef.getTime();
        System.out.println("计划时间为: " + runTime);
        TimerTask05 task = new TimerTask05();
        Timer timer = new Timer();
        timer.schedule(task, runTime, 2000);  // 任务，起始时间，循环间隔时间
    }
}