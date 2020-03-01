package com.test.wxx.factory.factorymethod;

import com.test.wxx.factory.simplyfactory.ICourse;
import com.test.wxx.factory.simplyfactory.PythonCourse;

public class PythonCourseFactory implements ICourseFactory {
    public ICourse create() {
        return new PythonCourse();
    }
}
