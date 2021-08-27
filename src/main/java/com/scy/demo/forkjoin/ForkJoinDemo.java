package com.scy.demo.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo {
    public static void main(String[] args) {
        MyTask task = new MyTask(0L, 10_000_000L);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> ans = forkJoinPool.submit(task);
        try {
            System.out.println(ans.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class MyTask extends RecursiveTask<Long> {

    private Long begin;
    private Long end;
    private final static int THRESHOLD = 1000;

    public MyTask(Long begin, Long end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long sum = 0;
        if (end - begin <= THRESHOLD){
            for (long i = begin; i < end; i++) {
                sum += i;
            }
        }else {
            long mid = begin + ((end - begin) >> 1);
            MyTask left = new MyTask(begin, mid);
            MyTask right = new MyTask(mid, end);
            ForkJoinTask<Long> lr = left.fork();
            ForkJoinTask<Long> rr = right.fork();
            sum += lr.join();
            sum += rr.join();
        }
        return sum;
    }
}