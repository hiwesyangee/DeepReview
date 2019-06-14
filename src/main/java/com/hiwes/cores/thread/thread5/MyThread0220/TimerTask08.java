package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer中的cancel().
 * 有时并不会停止计划任务，而是正常执行。
 * 原因: 有时候，Timer类中的cancel()方法，没有抢到queue锁，则让TimerTask类正常执行。
 */
public class TimerTask08 extends TimerTask {
    private int i;

    public TimerTask08(int i) {
        super();
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println("第 " + i + " 次没有被cancel取消.");
    }
}

class Run08 {
    public static void main(String[] args) {
        int i = 0;
        Calendar calendarRef = Calendar.getInstance();
        Date runTime1 = calendarRef.getTime();
        while (true) {
            i++;
            Timer timer = new Timer();
            TimerTask08 task = new TimerTask08(i);
            timer.schedule(task, runTime1);
            timer.cancel();
        }
    }
}