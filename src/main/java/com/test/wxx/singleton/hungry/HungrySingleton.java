package com.test.wxx.singleton.hungry;

/**
 * 类加载的时候初始化对象-饿汉式
 *
 * 优点：执行效率高，性能高，无锁
 * 缺点：某些情况可能造成内存浪费
 *
 * 单例应用
 * FactoryBean的管理
 * Mybatis的ErrorContext 错误上下文 -- ThreadLocal应用场景
 *
 * 单例模式有点：
 * 1.内存只有一个实例，减少内存开销
 * 2.可以避免对资源的多重占用
 * 3.设置全局访问，严格控制访问。
 * 缺点：
 * 1.没有接口，扩展困难
 * 2.如果要扩展单例对象，只有修改代码，没有其他途径
 *
 * 知识点总结：
 * 1.私有化构造器
 * 2.保证线程安全
 * 3.延迟加载
 * 4.防止序列化和反序列化破坏
 * 5.防御反射攻击单例
 */
public class HungrySingleton {

    private static final HungrySingleton INSTANCE = new HungrySingleton();

    private HungrySingleton(){

    }

    public static HungrySingleton getInstance(){
        return INSTANCE;
    }
}
