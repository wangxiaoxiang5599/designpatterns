package com.test.wxx.singleton;

import com.test.wxx.factory.simplyfactory.JavaCourse;
import com.test.wxx.singleton.register.ContainerSingleton;

public class ContainerSingletonTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object object1 = ContainerSingleton.getInstance(JavaCourse.class.getName());
        Object object2 = ContainerSingleton.getInstance(JavaCourse.class.getName());
        System.out.println(object1);
        System.out.println(object2);
    }
}
