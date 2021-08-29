package com.scy.demo.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinFibonacci {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        for (int i = 0; i < 10; i++) {
            ForkJoinTask<Integer> task = forkJoinPool.submit(new FibonacciTask(i));
            System.out.println(task.get());
        }

        ForkJoinTask<Integer> task = forkJoinPool.submit(new FibonacciTask(10));
        System.out.println(task.get());
    }
}


class FibonacciTask extends RecursiveTask<Integer> {

    int n;

    public FibonacciTask(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }
        FibonacciTask f1 = new FibonacciTask(n - 1);
        f1.fork();
        FibonacciTask f2 = new FibonacciTask(n - 2);
        f2.fork();

        return f1.join()  + f2.join();
    }
}