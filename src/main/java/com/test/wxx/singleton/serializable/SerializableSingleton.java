package com.test.wxx.singleton.serializable;

import java.io.Serializable;

/**
 *  序列花破坏单例
 *  测试类SerializableSingletonTest.java
 */
public class SerializableSingleton implements Serializable {

    private static final SerializableSingleton INSTANCE = new SerializableSingleton();

    private SerializableSingleton(){

    }

    public static SerializableSingleton getInstance(){
        return INSTANCE;
    }

    /**
     * 重写特定方法
     * 解决序列化破坏单例
     */
    /*private Object readResolve(){
        return INSTANCE;
    }*/
}
