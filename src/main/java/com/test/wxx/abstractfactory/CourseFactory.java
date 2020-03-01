package com.test.wxx.abstractfactory;

/**
 * 抽象工厂-复杂产品的工厂
 */
public abstract class CourseFactory {

    public CourseFactory()
    {
        System.out.println("初始化...");
    }

    protected abstract INote createNote();

    protected abstract IVideo createVideo();

}
