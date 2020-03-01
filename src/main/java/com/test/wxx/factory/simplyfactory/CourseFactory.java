package com.test.wxx.factory.simplyfactory;

/**
 * 简单工厂-产品的工厂
 */
public class CourseFactory {
    public static ICourse create(Class<? extends ICourse> clazz)
            throws IllegalAccessException, InstantiationException {
        if (clazz != null){
            return clazz.newInstance();
        }
        return null;
    }
}
