package com.scy.demo.singleton;

import com.scy.annoations.Recommand;
import com.scy.annoations.ThreadSafe;

/**
 * 类名： SingletonDemo <br>
 * 描述：使用枚举来获取单例 <br>
 *
 *  --》推荐：相比懒汉模式线程更加安全，相比饿汉，不浪费资源
 *
 * 创建日期： 2018/9/20 <br>
 *
 * @author suocaiyuan
 * @version V1.0
 */
@ThreadSafe
@Recommand
public class SingletonDemo {

    private SingletonDemo() {
    }

    public static SingletonDemo instance = new SingletonDemo();

    public static SingletonDemo getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        SingletonDemo singletonDemo = getInstance();
        SingletonDemo singletonDemo2 = getInstance();
        System.out.println(singletonDemo.hashCode());
        System.out.println(singletonDemo2.hashCode());
    }
}
