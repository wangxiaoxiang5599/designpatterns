package com.test.wxx.factorymethod;

import com.test.wxx.simplyfactory.ICourse;
import com.test.wxx.simplyfactory.JavaCourse;

public class JavaCourseFactory implements ICourseFactory {
    public ICourse create() {
        return new JavaCourse();
    }
}
