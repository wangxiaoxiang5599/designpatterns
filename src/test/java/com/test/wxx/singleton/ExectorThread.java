package com.test.wxx.singleton;

import com.test.wxx.singleton.lazy.LazySimpleSingleton;

public class ExectorThread implements Runnable {
    public void run() {
        LazySimpleSingleton instance = LazySimpleSingleton.getInstance();
        System.out.println(Thread.currentThread().getName() + ":" +instance);
    }
}
