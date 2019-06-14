package com.hiwes.cores.thread.thread1.Thread0213;

/**
 * getId()，用于获取线程的唯一标识
 */
public class MyThread12{
    public static void main(String[] args) {
        Thread runThread = Thread.currentThread();
        System.out.println(runThread.getName()+"  "+runThread.getId());
    }
}
