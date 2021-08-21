package com.scy.demo.designpattern.guard;

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

    //保护性线程，可以替换join模式
    public static void main(String[] args) {
        GuardObject guardObject = new GuardObject();
        new Thread(() -> {
            //等待结果
            log.debug("等待结果");
            List<String> list = (List<String>) guardObject.get(2000);
            log.debug("结果大小[{}]", list.size());
        }, "t1").start();

        new Thread(() -> {
            log.debug("执行下载");
            try {
                //替换join，等待的同时还可以做其他的
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
    // this.wait(设计)
    public Object get(long timeOut) {
        synchronized (this) {
            long cur = System.currentTimeMillis();
            long passedTime = 0;
            while (response == null) {
                //这一轮应该等待的时间
                long waitTime = timeOut - passedTime;
                //passedTime >= timeOut
                if (waitTime <= 0) {
                    break;
                }
                try {
                    // timeOut - passedTime
                    this.wait(waitTime);
                    // 不能使用this.wait(timeOut),防止虚假唤醒后，等待时间大于timeOut
                    // this.wait(timeOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - cur;
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
