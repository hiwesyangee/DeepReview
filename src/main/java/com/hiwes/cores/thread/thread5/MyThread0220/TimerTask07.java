package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 测试:
 * TimerTask中的cancel()。将自身任务在队列中进行清除。
 * Timer中的cancel()。将所有任务在队列中进行清除。
 */
public class TimerTask07 extends TimerTask {

    @Override
    public void run() {
        System.out.println("A run timer = " + new Date());
        this.cancel();  // 移除自身任务。
        System.out.println("A task 移除了自己.");
    }
}

class TimerTask07_2 extends TimerTask {

    @Override
    public void run() {
        System.out.println("B run timer = " + new Date());
    }
}


class run07 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("当前时间为: " + new Date());
        Calendar calendarRedf = Calendar.getInstance();
        Date runTime1 = calendarRedf.getTime();
        System.out.println("计划时间为: " + runTime1);

        TimerTask07 task1 = new TimerTask07();
        TimerTask07_2 task2 = new TimerTask07_2();
        Timer timer = new Timer();
        timer.schedule(task1, runTime1, 2000);
        timer.schedule(task2, runTime1, 2000);

        Thread.sleep(10000);
        timer.cancel();  // 移除所有任务
    }
}