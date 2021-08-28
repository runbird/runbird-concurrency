package com.scy.demo.blockquene;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类名： BlockingQueTest <br>
 * 描述：TODO <br>
 * 创建日期： 2019/11/4 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
public class BlockingQueueDemo02 {

    public static void main(String[] args) throws InterruptedException {
        // 声明一个容量为10的缓存队列
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);

        //new了三个生产者和一个消费者
        Producer producer1 = new Producer(queue, "producer1");
        Producer producer2 = new Producer(queue, "producer2");
        Producer producer3 = new Producer(queue, "producer3");
        Consumer consumer = new Consumer(queue);

        // 借助Executors
        ExecutorService service = Executors.newCachedThreadPool();
        // 启动线程
        service.execute(producer1);
        service.execute(producer2);
        service.execute(producer3);
        service.execute(consumer);

        // 执行10s
        Thread.sleep(10 * 1000);
        producer1.stop();
        producer2.stop();
        producer3.stop();

        Thread.sleep(2000);
        // 退出Executor
        service.shutdown();
    }
}

class Consumer implements Runnable {
    private BlockingQueue<String> queue;
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }


    @Override
    public void run() {
        Random random = new Random();
        System.out.println("生产者线程开始启动。。。。");
        boolean isRunning = true;
        try {
            while (isRunning) {
                System.out.println("正从队列获取数据...");
                //有数据时直接从队列的队首取走，无数据时阻塞，在2s内有数据，取走，超过2s还没数据，返回失败
                String data = queue.poll(2, TimeUnit.SECONDS);
                if (data != null) {
                    System.out.println("正在消费数据。。。");
                    System.out.println("消费数据为： " + data);
                    Thread.sleep(random.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                } else {
                    // 超过2s还没数据，认为所有生产线程都已经退出，自动退出消费线程。
                    isRunning = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.interrupted();
        } finally {
            System.out.println("退出消费者线程");
        }
    }
}

class Producer implements Runnable {

    private volatile boolean isRuning = true; //是否运行标志
    private BlockingQueue blockingQueue;
    private static AtomicInteger count = new AtomicInteger(1);
    // private AtomicInteger count = new AtomicInteger(1);
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;
    private final String name;

    public Producer(BlockingQueue blockingQueue, String name) {
        this.blockingQueue = blockingQueue;
        this.name = name;
    }

    @Override
    public void run() {
        Random random = new Random();
        String data = null;

        System.out.println("生产者线程启动。。。。");
        try {
            while (isRuning) {
                data = "data: " + count.incrementAndGet();
                Thread.sleep(random.nextInt(DEFAULT_RANGE_FOR_SLEEP));//取0~DEFAULT_RANGE_FOR_SLEEP值的一个随机数
                System.out.println(name + " 将数据：" + data + "放入队列...");
                if (!blockingQueue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.out.println("放入数据失败：" + data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("退出生产者线程！");
        }
    }

    public void stop() {
        isRuning = false;
    }
}