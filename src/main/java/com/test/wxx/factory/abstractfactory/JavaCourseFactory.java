package com.test.wxx.factory.abstractfactory;

public class JavaCourseFactory extends CourseFactory {
    protected INote createNote() {
        return new JavaNote();
    }

    protected IVideo createVideo() {
        return new JavaVideo();
    }
}
