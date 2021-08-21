package com.scy.demo.deadlock;

import lombok.extern.slf4j.Slf4j;

/**
 * 类名： DeadLockDemo <br>
 * 描述：死锁Demo
 *
 * 如何避免死锁发生
 * 1、首先，“互斥”是没有办法避免的，对于“占用且等待”这个条件，我们可以一次性申请所有的资源，这样就不存在等待了。
 *
 * 2、对于“不可抢占”这个条件，占用部分资源的线程进一步申请其他资源时，如果申请不到，可以在一定时间后，主动释放它占有的资源，这样就解决了不可抢占这个条件。
 *
 * 3、对于“循环等待”，我们可以靠按“次序”申请资源来预防。所谓按序申请，就是给资源设定顺序，申请的时候可以先申请序号小的资源，再申请序号大的，这样资源线性化后，自然就不存在循环等待了。<br>
 *
 * 创建日期： 2018/11/5 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
@Slf4j
public class DeadLockDemo implements Runnable {
    public int flag = 1;

    private static final Object o1 = new Object();
    private static final Object o2 = new Object();

    @Override
    public void run() {
        log.info("flag{}", flag);
        if (flag == 1) {
            synchronized (o1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    log.info("获取了O2 ", 2);
                }
            }
        }
        if (flag == 0) {
            synchronized (o2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    log.info("获取了O1 ", 1);
                }
            }
        }
    }


    public static void main(String[] args) {
        DeadLockDemo demo1 = new DeadLockDemo();
        DeadLockDemo demo2 = new DeadLockDemo();
        demo1.flag = 1;
        demo2.flag = 0;
        new Thread(demo1).start();
        new Thread(demo2).start();
    }
}
