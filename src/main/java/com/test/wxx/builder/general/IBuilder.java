package com.test.wxx.builder.general;

/**
 * 建造者抽象（Builder）：建造者的抽象类，规范产品对象的各个组成部分的建造，一般由子类
 * 实现具体的建造过程.
 */
public interface IBuilder {
    Product build();
}
