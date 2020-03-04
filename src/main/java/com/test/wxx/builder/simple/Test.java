package com.test.wxx.builder.simple;

public class Test {
    public static void main(String[] args) {
        CourseBuilder courseBuilder = new CourseBuilder();
        courseBuilder.addHomework("作业");
        courseBuilder.addName("wxx");
        courseBuilder.addNote("笔记");
        courseBuilder.addPpt("PPT");
        courseBuilder.addVideo("录像");
        System.out.println(courseBuilder.builder());
    }
}
