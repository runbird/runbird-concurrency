package com.scy.juc.aqs;

import com.scy.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

/**
 * 类名： CopyOnWriteArrayListDemo <br>
 * 描述：TODO <br>
 * 创建日期： 2018/9/28 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
@Slf4j
@ThreadSafe
public class CopyOnWriteArrayListDemo {

    private static int threadCilent = 5000;
    private static int localThread = 200;

    //适合读多写少的场景
    private static List<Integer> list = new CopyOnWriteArrayList<>();
    // @NotThreadSafe
    // private static List<Integer> list = new ArrayList<>();



    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(localThread);
        final CountDownLatch downLatch = new CountDownLatch(threadCilent);
        for (int i = 0; i < threadCilent; i++) {
            final int count = i;
            executor.execute(() -> {
                try {
                    semaphore.acquire();
                    //局部变量不一定是安全的
                    //因为间接操作了共享变量
                    update(count);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                downLatch.countDown();
            });
        }
        downLatch.await();
        executor.shutdown();
        log.info("<=== size {}", list.size());
    }

    private static void update(int i) {
        //非线程安全的list，这里不是原子操作
        list.add(i);
    }

    //    public boolean add(E e) {
    //        synchronized (lock) {
    //            Object[] es = getArray();
    //            int len = es.length;
    //            es = Arrays.copyOf(es, len + 1);
    //            es[len] = e;
    //            setArray(es);
    //            return true;
    //        }
    //    }
}
