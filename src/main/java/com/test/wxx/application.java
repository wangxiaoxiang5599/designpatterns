package com.test.wxx;

import com.test.wxx.abstractfactory.CourseFactory;
import com.test.wxx.abstractfactory.JavaCourseFactory;


public class application {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        /*ICourse course = CourseFactory.create(JavaCourse.class);
        course.study();

        ICourseFactory courseFactory = new PythonCourseFactory();
        ICourse course1 = courseFactory.create();
        course1.study();*/

        /*ILoggerFactory loggerFactory = new LoggerContext();
        Logger logger = loggerFactory.getLogger("a");*/

    }
}
