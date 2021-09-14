package com.scy.demo.guardpause;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * 类名： AlertAgent <br>
 * 描述：TODO <br>
 * 创建日期： 2021/9/14 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
public class AlertAgent {

    public void init() {
        Thread connectingThread = new Thread(new ConnectingTask());
        connectingThread.start();

        ScheduledThreadPoolExecutor heartbeatExecutor = new ScheduledThreadPoolExecutor(5,
                new ThreadFactory() {
                    private AtomicInteger index = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread();
                        thread.setName("heartbeat-thread-" + index);
                        thread.setDaemon(true);
                        return thread;
                    }
                });

        heartbeatExecutor.scheduleAtFixedRate(new HeartbeatTask(), 5000, 2000, TimeUnit.MILLISECONDS);
    }
}


class ConnectingTask implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("alarm connected");
        onConnected();
    }

    private void onConnected() {
        try {

        } catch (Exception e) {

        }
    }
}

class HeartbeatTask implements Runnable {

    private volatile boolean connectedToserver = false;

//    Predicate agentConnected = new Predicate() {
//        @Override
//        public boolean evaluate(Object o) {
//            return connectedToserver;
//        }
//    }

    @Override
    public void run() {
        if (!testConenction()) {
            onDisconnected();
            reconnected();
        }
    }

    private boolean testConenction() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test connection normal");
        return true;
    }


    private void onDisconnected() {
        connectedToserver = false;
    }


    private void reconnected() {
        ConnectingTask connectingTask = new ConnectingTask();
        connectingTask.run();
    }

}
