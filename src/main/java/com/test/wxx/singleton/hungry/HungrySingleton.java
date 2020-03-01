package com.test.wxx.singleton.hungry;

/**
 * 类加载的时候初始化对象-饿汉式
 *
 * 优点：执行效率高，性能高，无锁
 * 缺点：某些情况可能造成内存浪费
 */
public class HungrySingleton {

    private static final HungrySingleton hungrySingleton = new HungrySingleton();

    private HungrySingleton(){

    }

    public static HungrySingleton getInstance(){
        return hungrySingleton;
    }
}
