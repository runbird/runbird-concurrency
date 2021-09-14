package com.scy.demo.threadlocal.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类名： MyThreadLocal <br>
 * 描述：TODO <br>
 * 创建日期： 2021/9/14 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
public class MyThreadLocal<V> {
    private final Map<Thread, V> threadLocalMap = new ConcurrentHashMap<>();

    public V get() {
        return get(Thread.currentThread());
    }

    private V get(Thread thread) {
        return threadLocalMap.get(thread);
    }

    public void set(V value) {
        set(Thread.currentThread(), value);
    }

    public void set(Thread thread, V value) {
        threadLocalMap.put(thread, value);
    }
}
