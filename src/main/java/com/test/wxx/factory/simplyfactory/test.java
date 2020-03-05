package com.test.wxx.factory.simplyfactory;

public class test {
    public static void main(String[] args) {
        try {
            ICourse course = CourseFactory.create(PythonCourse.class);
            course.study();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
