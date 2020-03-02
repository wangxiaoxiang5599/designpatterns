package com.test.wxx.builder.general;

public class ConcreteBuilder implements IBuilder {
    private Product product = new Product();

    public Product build() {
        return product;
    }
}
