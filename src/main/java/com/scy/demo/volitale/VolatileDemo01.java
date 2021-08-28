package com.scy.demo.volitale;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class VolatileDemo01 {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> storage = new ArrayBlockingQueue<>(8);

        Producer producer = new Producer(storage);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        Thread.sleep(500);

        Consumer consumer = new Consumer(storage);
        while (consumer.needNum()) {
            System.out.println(consumer.storage.take() + " —> 被消费了");
            Thread.sleep(100);
        }
        System.out.println("<--- 消费结束");

        //生产者线程因为storage阻塞，导致不能正常结束
        //故在这种情况下可以考虑使用interrupted
        //避免使用 stop() suspend() 和 resume()方法，有可能导致死锁
        //volatile在线程被阻塞的情况下无法感受中断
        producer.canceled = true;
        System.out.println(producer.canceled);
    }

}

class Producer implements Runnable {

    BlockingQueue<Integer> storage;

    public volatile boolean canceled = false;

    public Producer(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        int num = 0;
        try {
            while (num <= 100_000 && !canceled) {
                if (num % 50 == 0) {
                    //blockQueue 阻塞导致不能正常结束
                    storage.put(num);
                    System.out.println(num + "是50的倍数，被放到了仓库中");
                }
                num++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("生产者结束运行");
        }
    }
}

class Consumer {
    BlockingQueue<Integer> storage;

    public Consumer(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }

    public boolean needNum() {
        return !(Math.random() > 0.97);
    }
}