package com.test.wxx.factory.abstractfactory;

public class test {
    public static void main(String[] args) {
        CourseFactory factory = new JavaCourseFactory();
        factory.createNote().edit();
        factory.createVideo().record();
    }
}
