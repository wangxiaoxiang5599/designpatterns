package com.test.wxx.singleton.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册式单例
 * 解决枚举式内存浪费情况
 * 会有线程安全问题-双重检查加锁可以解决
 */
public class ContainerSingleton {

    private ContainerSingleton(){}

    private static Map<String, Object> ioc = new ConcurrentHashMap<String, Object>();

    public static Object getInstance(String className)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object o = null;
        /*if (!ioc.containsKey(className)){
           o =  Class.forName(className).newInstance();
           ioc.put(className, o);
           return o;
        }
        return ioc.get(className);*/
        if(ioc.containsKey(className)){
            return ioc.get(className);
        }
        synchronized (ContainerSingleton.class){
            if(ioc.containsKey(className)){
                return ioc.get(className);
            }
            o =  Class.forName(className).newInstance();
            ioc.put(className, o);
            return o;
        }
    }
}
