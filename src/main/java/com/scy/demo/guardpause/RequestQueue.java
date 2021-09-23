package com.scy.demo.guardpause;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 类名： RequestQueue <br>
 * 描述： <br>
 * 创建日期： 2021/9/22 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
public class RequestQueue {

    private static final Integer MAX_VALUE = Integer.MAX_VALUE;

    private Queue<Request> queue = new ArrayBlockingQueue<>(MAX_VALUE);

    ReentrantLock lock = new ReentrantLock();

    Condition condition = lock.newCondition();

    /**
     * 阻塞式获取元素 ex:ArrayBlockingQueue  take()
     */
    public Request get() {
        Request request = null;
        this.lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            request = queue.poll();
            condition.signalAll();
        } catch (Exception e) {
            condition.signalAll();
        } finally {
            this.lock.unlock();
        }
        return request;
    }

    /**
     * 阻塞式添加元素 ex:ArrayBlockingQueue  put()
     */
    public void put(Request request) {
        this.lock.lock();
        try {
            while (queue.size() >= MAX_VALUE) {
                condition.await();
            }
            queue.offer(request);
            condition.signalAll();
        } catch (Exception e) {
            condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }
}

class Request {

}