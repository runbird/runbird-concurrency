package com.scy.demo.twophase;

/**
 * 类名： TwoPhaseThreadExecutor <br>
 * 描述： <br>
 * 创建日期： 2021/9/23 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
public class TwoPhaseThreadExecutor {

    private Thread executeThread;

    private volatile boolean isRunning = false;

    /**
     * task 发生阻塞的线程任务
     */
    public void execute(Runnable task) {
        executeThread = new Thread(() -> {
            Thread childThread = new Thread(task);
            childThread.setDaemon(true);
            childThread.start();
            try {
                // 强行执行子线程，使其进入休眠状态
                childThread.join();
                isRunning = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executeThread.start();
    }

    public void shutdown(long mills) {
        long currentTime = System.currentTimeMillis();
        while (!isRunning) {
            if (System.currentTimeMillis() - currentTime >= mills) {
                System.out.println("任务超时，需要结束！");
                executeThread.interrupt();
                break;
            }
        }
        isRunning = false;
    }

    public static void main(String[] args) {
        TwoPhaseThreadExecutor executor = new TwoPhaseThreadExecutor();
        long start = System.currentTimeMillis();
        executor.execute(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown(1000);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
