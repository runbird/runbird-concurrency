package com.scy.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 类名： AQSDemo <br>
 * 描述：TODO <br>
 * 创建日期： 2021/6/22 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
@Slf4j
public class AQSDemo {
    static ReentrantLock lock = new ReentrantLock(true);

    static void demo() {
        new Thread(() -> {
            try {
                lock.lock();
                log.debug("----->");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();
    }

    public static void main(String[] args) {
        AQSDemo.demo();
    }
}
