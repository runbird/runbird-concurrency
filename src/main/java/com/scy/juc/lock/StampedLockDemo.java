package com.scy.juc.lock;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * 类名： StampedLockDemo <br>
 * 描述：
 * StampedLock与ReadWriteLock相比，在读的过程中也允许后面的一个线程获取写锁对共享变量进行写操作，为了避免读取的数据不一致，
 * 使用StampedLock读取共享变量时，需要对共享变量进行是否有写入的检验操作，并且这种读是一种乐观读。
 *
 * StampedLock是一种在读取共享变量的过程中，允许后面的一个线程获取写锁对共享变量进行写操作，使用乐观读避免数据不一致的问题，
 * 并且在读多写少的高并发环境下，比ReadWriteLock更快的一种锁。<br>
 * 创建日期： 2021/8/16 <br>
 * ${source} : https://mp.weixin.qq.com/s/9tesh-GYta7wHPx2R6Fk6Q
 * @author suocaiyuan
 * @version V1.0
 */
public class StampedLockDemo {

    public StampedLock stampedLock = new StampedLock();

    private double x, y;

    public void testGetAndReleaseReadLock() {
        long stampLock = stampedLock.readLock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stampedLock.unlockRead(stampLock);
        }
    }

    public void testGetAndReleaseWriteLock() {
        long writeLock = stampedLock.writeLock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stampedLock.unlockWrite(writeLock);
        }
    }

    void move(double deltaX, double deltaY) {
        long stamp = stampedLock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        }finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    /**
     * 这种将乐观读升级为悲观读锁的方式相比一直使用乐观读的方式更加合理，如果不升级为悲观读锁，
     * 则程序会在一个循环中反复执行乐观读操作，直到乐观读操作期间没有线程执行写操作，
     * 而在循环中不断的执行乐观读会消耗大量的CPU资源，升级为悲观读锁是更加合理的一种方式。
     * @return
     */
    double distanceFromOrigin() {
        long stamp = stampedLock.tryOptimisticRead();
        double currentX = x , currentY = y;
        // 如果有线程对共享变量进行了写操作
        // 则 v.validate(stamp) 返回false
        if (!stampedLock.validate(stamp)) {
            //乐观锁升级为悲观锁
            stamp = stampedLock.readLock();
            try {
                currentX = x;
                currentY = y;
            }finally {
                //释放悲观锁
                stampedLock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    void moveIfAtOrigin(double newX, double newY) {
        long stamp = stampedLock.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                // a valid write stamp, or zero on failure
                long ws = stampedLock.tryConvertToWriteLock(stamp);
                //不为零表示转化失败
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                } else {
                    stampedLock.unlockRead(stamp);
                    stamp = stampedLock.writeLock();
                }
            }
        }finally {
            stampedLock.unlock(stamp);
        }
    }

    /**
     * 如果某个线程阻塞在StampedLock的readLock()或者writeLock()方法上时，此时调用阻塞线程的interrupt()方法中断线程，会导致CPU飙升到100%
     */
    public void testSample() throws InterruptedException {
        final StampedLock lock = new StampedLock();

        Thread t1 = new Thread(()->{
            lock.writeLock();

            // 永远阻塞在此处，不释放写锁
            LockSupport.park();
        });
        t1.start();
        Thread.sleep(100);

        Thread t2 = new Thread(lock::readLock);
        t2.start();
        Thread.sleep(100);

        //中断线程thread02 会导致线程thread02所在CPU飙升
        t2.interrupt();
        //应该使用  lock.readLockInterruptibly() 或 lock.writeLockInterruptibly()
        t2.join();
    }
}
