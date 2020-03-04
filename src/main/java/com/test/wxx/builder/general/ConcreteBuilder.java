package com.test.wxx.builder.general;

/**
 * 建造者（ConcreteBuilder）:具体的Builder类，根据不同的业务逻辑，具体化对象的各个组成
 * 部分的创建。
 *
 * 建造者模式适用于一个具有较多的零件的复杂产品的创建过程，由于需求的变化，组成这个复杂产
 * 品的各个零件经常猛烈变化，但是它们的组合方式却相对稳定。
 *
 * 相同的方法，不同的执行顺序，产生不同的结果时
 * 多个部件或零件，都可以装配到一个对象中，但是产生的结果又不相同。
 * 产品类非常复杂，或者产品类中的调用顺序不同产生不同的作用。
 * 当初始化一个对象特别复杂，参数多，而且很多参数都具有默认值时。
 * 建造者模式，只关注用户需要什么，将最少的关键字传过来，生成你想要的结果。
 *
 * 实际顺序是在build方法里面。那是顺序和条件都确定了。每个顺序和条件都分别存储下来了。
 * 判断有没有，有就添加到product后面。当然就是先判断条件再判断order顺序了
 */
public class ConcreteBuilder implements IBuilder {

    private Product product = new Product();

    public Product build() {
        return product;
    }
}
