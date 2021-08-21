package com.scy.demo.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 破坏不可抢占条件 : 需要发生死锁的线程能够主动释放它占有的资源
 */
public class DeadLockDemo03 {
    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        Thread a = new Thread(new Task1());
        Thread b = new Thread(new Task2());
        a.start();
        b.start();
    }

    static class Task1 implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("Task1 running");
                while (true) {
                    if (lock1.tryLock(1, TimeUnit.MILLISECONDS)) {
                        System.out.println("Task1 lock obj1");
                        //Thread.sleep(300);
                        if (lock2.tryLock(1, TimeUnit.MILLISECONDS)) {
                            System.out.println("Task2 lock obj2");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    static class Task2 implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("Task2 running");
                while (true) {
                    if (lock2.tryLock(1, TimeUnit.MILLISECONDS)) {
                        System.out.println("Task2 lock obj2");
                        //Thread.sleep(300);
                        if (lock1.tryLock(1, TimeUnit.MILLISECONDS)) {
                            System.out.println("Task1 lock obj1");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

}
