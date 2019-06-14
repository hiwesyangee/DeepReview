package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 使用schedule(TimerTask task, long delay).
 * 在当前时间的基础上，延迟指定毫秒数后执行“一次”TimerTask任务。
 */
public class TimerTask09 extends TimerTask {
    @Override
    public void run() {
        System.out.println("运行了! 时间为: " + new Date());
    }

}

class Run09 {
    public static void main(String[] args) {
        TimerTask09 task = new TimerTask09();
        Timer timer = new Timer();
        System.out.println("当前时间为: " + new Date());
        timer.schedule(task, 7000);
    }
}
