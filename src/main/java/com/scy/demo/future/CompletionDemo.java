package com.scy.demo.future;

import java.util.concurrent.*;

/**
 * 类名： CompletionDemo <br>
 * 描述： <br>
 * 创建日期： 2021/9/20 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
public class CompletionDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executorService);
        System.out.println("开始获取结果");

        Future<String> future_10 = completionService.submit(() -> {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("执行时长为10S任务完成");
            return "10";
        });

        Future<String> future_5 = completionService.submit(() -> {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("执行时长为5S任务完成");
            return "5";
        });

        Future<String> future_15 = completionService.submit(() -> {
            TimeUnit.SECONDS.sleep(15);
            System.out.println("执行时长为15S任务完成");
            return "15";
        });
        TimeUnit.SECONDS.sleep(1);
        System.out.println("执行完毕");

        for (int i = 0; i < 3; i++) {
            String res = completionService.take().get();
            System.out.println("获取结果：" + res);
        }
        Thread.currentThread().join();
    }
}
