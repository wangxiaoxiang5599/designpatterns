package com.test.wxx.singleton.lazy;

/**
 * 双重检查懒汉式
 *
 * 优点：性能高，线程安全
 * 缺点：可读性难度增加，不够优雅
 */
public class LazyDoubleCheckSingleton {

    // volatile解决指令重排序问题
    private volatile static LazyDoubleCheckSingleton instance;

    private LazyDoubleCheckSingleton(){}

    public static LazyDoubleCheckSingleton getInstance(){
        if(instance == null){
            synchronized(LazyDoubleCheckSingleton.class){
                if(instance == null){
                    instance = new LazyDoubleCheckSingleton();
                }
            }
        }
        return instance;
    }
}
