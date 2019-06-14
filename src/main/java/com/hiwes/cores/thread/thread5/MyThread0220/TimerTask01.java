package com.hiwes.cores.thread.thread5.MyThread0220;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器Timer的使用:
 * 主要负责计划任务的功能，也就是在指定时间开始执行某一任务，方法列表如下:
 * 1）cancel().
 * 2）equals().
 * 3）getClass().
 * 4）hashCode().
 * 5）notify().
 * 6）notifyAll().
 * 7）purge().
 * 8）schedule(TimerTask task, Date time)..在指定日期执行一次某一任务。
 * 9）schedule(TimerTask task, long delay).在当前时间的基础上，延迟指定毫秒数后执行“一次”TimerTask任务。
 * 10）schedule(TimerTask task, Date firstTime, long delay).在指定的日期，按执行的间隔周期，无限循环执行某一任务。
 * 11）schedule(TimerTask task, long delay, long period).在当前时间的基础上，延迟指定毫秒数后，无限循环执行某一任务。
 * 12）scheduleAtFixedRate(TimerTask task, Date firstTime, long period).
 * 13）scheduleAtFixedRate(TimerTask task, long delay, long period).
 * 14）toString().
 * 15）wait().
 * 16）wait(long timeout).
 * 17）wait(long timeout, int nanos).
 * Timer的主要作用是设置计划任务，但封装任务的类却是TimerTask。执行计划任务的代码要放到TimerTask的子类中，
 * 因为TimerTask是一个抽象类。
 * <p>
 * schedule(TimerTask task, Date time).在指定日期执行一次某一任务。
 * <p>
 * 1）执行任务的时间晚于当前时间————在未来执行的效果。
 */
public class TimerTask01 extends TimerTask {

    @Override
    public void run() {
        System.out.println("任务执行了，时间为:" + new Date());
    }
}


class Run01 {
    public static void main(String[] args) {
        System.out.println("当前时间为:" + new Date());
        Calendar calendarRef = Calendar.getInstance();
        calendarRef.add(Calendar.SECOND, 3);
        Date runDate = calendarRef.getTime();

        TimerTask01 task = new TimerTask01();
        Timer timer = new Timer();
        timer.schedule(task, runDate); // schedule(TimerTask task, Date time).在指定日期执行一次某一任务。
    }
}

class Run01_2 {
    public static void main(String[] args) {
        System.out.println("当前时间为:" + new Date());
        Calendar calendarRef = Calendar.getInstance();
        calendarRef.add(Calendar.SECOND, 3);
        Date runDate = calendarRef.getTime();

        TimerTask01 task = new TimerTask01();
        Timer timer = new Timer(true);
        timer.schedule(task, runDate); // schedule(TimerTask task, Date time).在指定日期执行一次某一任务。
    }
}