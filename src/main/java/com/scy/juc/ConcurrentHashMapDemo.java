package com.scy.juc;

import com.scy.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

import static javax.swing.UIManager.put;

/**
 * 类名： ConcurrentHashMapDemo <br>
 * 描述：TODO <br>
 * 创建日期： 2018/9/28 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
@Slf4j
@ThreadSafe
public class ConcurrentHashMapDemo {
    private static int clientTotal = 5000000;
    private static int threadTotal = 12;
    private static Map<Integer, Integer> map = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        log.info("<=== startTime {}", start);
        ExecutorService service = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(threadTotal);
        CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            int x = i;
            int y = i;
            service.execute(() -> {
                try {
                    semaphore.acquire();
                    update(x, y);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        service.shutdown();
        log.info("<=== size {}", map.size());
        log.info("<=== startTime {}", System.currentTimeMillis() - start);
    }

    private static void update(int x, int y) {
        map.put(x, y);
    }


}
