package com.test.wxx.factorymethod;

import com.test.wxx.simplyfactory.ICourse;
import com.test.wxx.simplyfactory.PythonCourse;

public class PythonCourseFactory implements ICourseFactory {
    public ICourse create() {
        return new PythonCourse();
    }
}
