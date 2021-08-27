package com.scy.demo.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 类名： ABAProblem <br>
 * 描述：TODO <br>
 * 创建日期： 2020/8/13 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
public class ABAProblem {

    private static AtomicInteger atomic = new AtomicInteger(100);
    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 0);

    public static void main(String[] args) {
        new Thread(() -> {
            atomic.compareAndSet(100, 101);
            atomic.compareAndSet(101, 100);
        }, "t1").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                boolean result = atomic.compareAndSet(100, 101);
                System.out.println("<===== atomic: " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();


        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicStampedReference.compareAndSet(100, 101,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            atomicStampedReference.compareAndSet(101, 100,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
        }, "asr1").start();

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println("<===== before atomicStampedReference: " + stamp);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int stamp2 = atomicStampedReference.getStamp();
            System.out.println("<===== after atomicStampedReference: " + stamp2);

            boolean asrResult = atomicStampedReference.compareAndSet(100, 101, stamp, stamp + 1);
            System.out.println("<===== atomicStampedReference: " + asrResult);
        }, "asr2").start();
    }
}
