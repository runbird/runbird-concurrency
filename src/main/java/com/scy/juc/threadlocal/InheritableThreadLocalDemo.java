package com.scy.juc.threadlocal;

import lombok.extern.slf4j.Slf4j;

/**
 * 类名： InheritableThreadLocalDemo <br>
 * 描述：TODO <br>
 * 创建日期： 2019/1/31 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
@Slf4j
public class InheritableThreadLocalDemo {

    private static final ThreadLocal<String> userId = new InheritableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        userId.set("id in main thread");

        Thread t2 = new Thread(() -> {
            log.info("thread02 get userId {}", userId.get());
        });

        Thread t1 = new Thread(() -> {
            log.info("thread01 get userId {}", userId.get());
            t2.start();
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t1.join();
    }
}
