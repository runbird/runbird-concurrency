package com.scy;

/**
 * @desc: test
 * @program: concurrency
 * @author: Suocaiyuan
 * @date: 2020-04-16 00:14
 **/
public class DemoTest {

    public void test1(){
        int a = 0;
        int b = 0;
        while (a < 20 && b < 20) {
            a += 1;
            b += 1;
        }
        System.out.println(a);
        System.out.println(b);
    }

    public static void main(String[] args) {
        DemoTest demo = new DemoTest();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(demo::test1);

        }
    }
}
