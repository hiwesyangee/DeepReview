package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 使用schedule(TimerTask task, Date firstTime, long delay).
 * 3）当任务执行时间被延时.  则后续队列所有任务都会往后推移。
 */
public class TimerTask06 extends TimerTask {
    @Override
    public void run() {
        try {
            System.out.println("A begin timer = " + new Date());
            Thread.sleep(5000);
            System.out.println("A   end timer = " + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Run06{
    public static void main(String[] args) {
        System.out.println("当前时间为: "+new Date());
        Calendar calendarRef = Calendar.getInstance();
        calendarRef.add(Calendar.SECOND,10);
        Date runTime = calendarRef.getTime();
        System.out.println("计划时间为: "+runTime );
        TimerTask06 task = new TimerTask06();
        Timer timer = new Timer();
        timer.schedule(task,runTime,3000);
    }
}