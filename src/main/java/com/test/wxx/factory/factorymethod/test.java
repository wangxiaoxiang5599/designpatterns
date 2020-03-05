package com.test.wxx.factory.factorymethod;

public class test {
    public static void main(String[] args) {
        ICourseFactory courseFactory = new JavaCourseFactory();
        courseFactory.create().study();
    }
}
