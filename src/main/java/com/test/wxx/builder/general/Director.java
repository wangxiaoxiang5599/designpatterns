package com.test.wxx.builder.general;

import java.lang.reflect.Method;

public class Director extends Object {
    public static void main(String[] args) {
        IBuilder builder= new ConcreteBuilder();
        System.out.println(builder.build());
    }
}
