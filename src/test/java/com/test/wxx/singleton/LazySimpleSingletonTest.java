package com.test.wxx.singleton;

/**
 * 多线程DEBUG
 * 右键DEBUG红点-》从ALL切换到Thread
 */
public class LazySimpleSingletonTest {
    public static void main(String[] args) {
        Thread t1 = new Thread(new ExectorThread());
        Thread t2 = new Thread(new ExectorThread());
        t1.start();
        t2.start();
        System.out.println("End");
    }
}
