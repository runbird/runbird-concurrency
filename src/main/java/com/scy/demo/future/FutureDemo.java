package com.scy.demo.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 类名： FutureDemo <br>
 * 描述： <br>
 * 创建日期： 2021/9/20 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
public class FutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<Integer>> list = new ArrayList<>();

        Future<Integer> future_15 = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(15);
            System.out.println("执行时长为15S任务完成");
            return 15;
        });
        list.add(future_15);

        Future<Integer> future_5 = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("执行时长为5S任务完成");
            return 5;
        });
        list.add(future_5);

        Future<Integer> future_10 = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("执行时长为10S任务完成");
            return 10;
        });
        list.add(future_10);

        System.out.println("开始获取结果");
        for (Future<Integer> future : list) {
            System.out.println("future.get() : " + future.get());
        }
        Thread.currentThread().join();
    }
}
