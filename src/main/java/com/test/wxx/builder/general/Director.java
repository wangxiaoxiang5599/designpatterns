package com.test.wxx.builder.general;

/**
 * 调用者（Director）：调用具体的建造者，来创建对象的各个部分，在指导者中不涉及具体产品
 * 的信息，只负责保证对象各部分完整创建或按某种顺序创建。
 */
public class Director extends Object {
    public static void main(String[] args) {
        IBuilder builder= new ConcreteBuilder();
        System.out.println(builder.build());
    }
}
