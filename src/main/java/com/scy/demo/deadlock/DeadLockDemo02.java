package com.scy.demo.deadlock;

import java.util.ArrayList;
import java.util.List;

/**
 * 破坏占用且等待条件
 */
public class DeadLockDemo02 {
    public static void main(String[] args) {
        Account a = new Account();
        Account b = new Account();
        a.transfer(b,100);
        b.transfer(a,200);
    }

    static class Allocator {
        private List<Object> als = new ArrayList<>();

        //一次性申请资源
        synchronized boolean apply(Object from, Object to) {
            if (als.contains(from) || als.contains(to)) {
                return false;
            } else {
                als.add(from);
                als.add(to);
            }
            return true;
        }

        synchronized void clean(Object from, Object to) {
            als.remove(from);
            als.remove(to);
        }
    }

    private void Allocator(){}
/**    枚举法单例    */
//    private enum SingleToHoler{
//        INSTANCE;
//
//        private Allocator instance;
//
//        SingleToHoler(){ instance = new Allocator(); }
//
//        public Allocator getInstance() {
//            return instance;
//        }
//    }
//
//    private static Allocator getInstance() {
//        return SingleToHoler.INSTANCE.getInstance();
//    }

    private static class SingleTonHoler {
        private static Allocator INSTANCE = new Allocator();
    }

    public static Allocator getInstance() {
        return SingleTonHoler.INSTANCE;
    }

    static class Account{
        private Allocator actr = DeadLockDemo02.getInstance();
        private int balance;

        void transfer(Account target, int amt) {
            while (!actr.apply(this, target)) {
                try {
                    synchronized (this) {
                        System.out.println(this.toString() + " lock obj1");
                        synchronized (target) {
                            System.out.println(this.toString() + " lock obj2");
                            if (this.balance > amt) {
                                this.balance -= amt;
                                target.balance += amt;
                            }
                        }
                    }
                } finally {
                    actr.clean(this, target);
                }
            }
        }
    }
}

