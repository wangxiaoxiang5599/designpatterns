package com.test.wxx.singleton;

import com.test.wxx.singleton.lazy.LazyStaticInnerClassSingleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 反射破坏私有构造
 */
public class ReflectTest {
    public static void main(String[] args)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException,
            InvocationTargetException {
        Class clazz = LazyStaticInnerClassSingleton.class;
        Constructor c = clazz.getDeclaredConstructor(null);
        c.setAccessible(true);
        Object o = c.newInstance();
        System.out.println(o);
    }
}
