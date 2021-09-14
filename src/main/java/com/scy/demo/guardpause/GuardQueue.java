package com.scy.demo.guardpause;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 类名： GuardQueue <br>
 * 描述：保护性暂停模式 <br>
 * 创建日期： 2021/9/14 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
public class GuardQueue {

    private final Queue<Integer> sourceList;

    public GuardQueue() {
        this.sourceList = new LinkedBlockingQueue<>();
    }

    public synchronized Integer get() {
        //轮询while(true)”的方式来等待某个状态，其实都可以用这个“等待-通知”机制来优化。
        while (sourceList.isEmpty()) {
            try {
                //等待
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sourceList.peek();
    }

    public synchronized void put(Integer i) {
        sourceList.add(i);
        //通知继续执行
        notifyAll();
    }

    public static void main(String[] args) throws InterruptedException {
        GuardQueue queue = new GuardQueue();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit((Runnable) queue::get);

        Thread.sleep(2000);
        executorService.submit(()->{
            queue.put(20);
        });

        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }
}
