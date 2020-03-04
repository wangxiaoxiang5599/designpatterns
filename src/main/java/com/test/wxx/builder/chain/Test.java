package com.test.wxx.builder.chain;

public class Test {
    public static void main(String[] args) {
        CourseBuilder builder = new CourseBuilder();
        builder.addHomework("zuoye")
                .addName("wxx")
                .addNote("note")
                .addPpt("ppt")
                .addVideo("video");
        System.out.println(builder.builder());
    }
}
