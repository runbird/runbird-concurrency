package com.scy.demo.deadlock;

/**
 * 破坏循环等待条件 : 只需要对系统中的资源进行统一编号，进程可在任何时刻提出资源申请，必须按照资源的编号顺序提出。
 * “资源有序分配法”。
 */
public class DeadLockDemo04 {
    public static void main(String[] args) {
        Account from = new Account(1, 100);
        Account to = new Account(2, 0);
        from.transfer(from,100);
        to.transfer(from,100);
    }
}

class  Account{
    private int id;
    private int balance;

    public Account(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    void transfer(Account target, int amt) {
        Account left = this;
        Account right = target;

        if (this.id > target.id) {
            left = target;
            right = this;
        }

        synchronized (left) {
            synchronized (right) {
                if (this.balance > amt) {
                    this.balance -= amt;
                    target.balance += amt;
                }
            }
        }
    }
}