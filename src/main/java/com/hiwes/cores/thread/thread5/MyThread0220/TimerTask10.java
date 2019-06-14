package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 使用schedule(TimerTask task, long delay, long period).
 * 在当前时间的基础上，延迟指定毫秒数后，无限循环执行某一任务。
 */
public class TimerTask10 extends TimerTask {
    @Override
    public void run() {
        System.out.println("运行了！时间为: "+new Date());
    }
}

class Run10{
    public static void main(String[] args) {
        TimerTask10 task = new TimerTask10();
        Timer timer = new Timer();
        timer.schedule(task,2000,1000);
    }
}