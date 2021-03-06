package com.test.wxx.factory.abstractfactory;

public class PythonCourseFactory extends CourseFactory {
    protected INote createNote() {
        return new PythonNote();
    }

    protected IVideo createVideo() {
        return new PythonVideo();
    }
}
