package com.test.wxx.factory.factorymethod;

import com.test.wxx.factory.simplyfactory.ICourse;
import com.test.wxx.factory.simplyfactory.JavaCourse;

public class JavaCourseFactory implements ICourseFactory {
    public ICourse create() {
        return new JavaCourse();
    }
}
