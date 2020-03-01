package com.test.wxx.factory.abstractfactory;

/**
 * 抽象工厂-复杂产品的工厂
 *
 * ILoggerFactory 工厂方法模式
 * 数据库连接池 工厂模式？ 那种工厂模式？
 */
public abstract class CourseFactory {

    public CourseFactory()
    {
        System.out.println("初始化...");
    }

    protected abstract INote createNote();

    protected abstract IVideo createVideo();

}
