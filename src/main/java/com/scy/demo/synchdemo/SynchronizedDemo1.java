package com.scy.demo.synchdemo;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc:
 * @program: concurrency
 * @author: Suocaiyuan
 * @date: 2020-06-24 22:46
 **/
@Slf4j(topic = "c.SynchronizedDemo1")
public class SynchronizedDemo1 {
    static final Object object = new Object();
    static boolean hasCigeratte = false;
    static boolean hasTask = false;

    //虚假唤醒
    public static void main(String[] args) throws InterruptedException {
        new Thread(() ->{
            synchronized (object) {
                log.debug("有烟没[{}]",hasCigeratte);
                if (!hasCigeratte) {
                    log.debug("没有烟任务等待");
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没[{}]",hasCigeratte);
                if (!hasCigeratte) {
                    log.debug("没烟");
                } else {
                    log.debug("有烟");
                }

            }
        },"t1").start();

        new Thread(() ->{
            synchronized (object) {
                log.debug("有外卖没[{}]",hasTask);
                if (!hasTask) {
                    log.debug("没有外卖任务等待");
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有外卖[{}]",hasTask);
                if (!hasTask) {
                    log.debug("没外卖");
                } else {
                    log.debug("有外卖");
                }

            }
        },"t2").start();

        Thread.sleep(1);

        new Thread(()->{
            //随机唤醒一个
            synchronized (object) {
                hasCigeratte = true;
                log.debug("香烟到了");
                object.notify();
            }
        },"t3").start();
    }

}
