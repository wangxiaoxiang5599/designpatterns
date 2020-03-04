package com.test.wxx.builder.chain;

import lombok.Data;

/**
 * 建造者模式通常是采用链式编程的方式构造对象
 *
 * 源码体现
 * JDK的StringBuilder
 * MyBatis的CacheBuilder
 *
 * 建造者模式的优点：
 * 1、封装性好，创建和使用分离；
 * 2、扩展性好，建造类之间独立、一定程度上解耦。
 * 建造者模式的缺点：
 * 1、产生多余的Builder对象；
 * 2、产品内部发生变化，建造者都要修改，成本较大。
 *
 * 建造者模式：适用于对象创建需要动态拼接复杂属性值的业务场景。
 * 例如：SQL拼接，链式编程，NIO，StringBuilder拼接字符串。
 *
 */
public class CourseBuilder {

    private Course course = new Course();

    public CourseBuilder addName(String name){
        course.setName(name);
        return this;
    }

    public CourseBuilder addPpt(String ppt){
        course.setPpt(ppt);
        return this;
    }

    public CourseBuilder addVideo(String video){
        course.setVideo(video);
        return this;
    }

    public CourseBuilder addNote(String note){
        course.setNote(note);
        return this;
    }

    public CourseBuilder addHomework(String homework){
        course.setHomework(homework);
        return this;
    }

    public Course builder(){
        return course;
    }

    @Data
    private class Course {
        private String name;
        private String ppt;
        private String video;
        private String note;
        private String homework;
    }

}
