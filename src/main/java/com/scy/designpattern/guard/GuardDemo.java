package com.scy.designpattern.guard;

import com.scy.utils.Downloder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @desc:
 * @program: concurrency
 * @author: Suocaiyuan
 * @date: 2020-06-24 23:34
 **/
@Slf4j(topic = "c.GuardDemo")
public class GuardDemo {

    public static void main(String[] args) {
        GuardObject guardObject = new GuardObject();
        new Thread(() -> {
            //等待结果
            log.debug("等待结果");
            List<String> list = (List<String>) guardObject.get();
            log.debug("结果大小[{}]", list.size());
        }, "t1").start();

        new Thread(() -> {
            log.debug("执行下载");
            try {
                List<String> result = Downloder.download();
                guardObject.complete(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t2").start();
    }

}

class GuardObject {
    private Object response;

    //获取结果
    public Object get() {
        synchronized (this) {
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}
