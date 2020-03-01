package com.test.wxx.singleton.lazy;

/**
 * 懒汉式-被外部调用时才创建实例（无synchronized）
 *
 * 优点：节省内存
 * 缺点：线程不安全
 */
public class LazySimpleSingleton {

    private static LazySimpleSingleton instance;

    private LazySimpleSingleton(){}

    public static LazySimpleSingleton getInstance(){
        if(instance == null){//多线程调用时，线程不安全
            instance = new LazySimpleSingleton();
        }
        return instance;
    }

    /**
     * synchronized 保证安全
     * 性能较差，所有实例全部阻塞
     * @return
     */
    /*public synchronized static LazySimpleSingleton getInstance(){
        if(instance == null){
            instance = new LazySimpleSingleton();
        }
        return instance;
    }*/
}
