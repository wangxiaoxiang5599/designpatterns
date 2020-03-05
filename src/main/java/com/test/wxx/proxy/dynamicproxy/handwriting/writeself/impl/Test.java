package com.test.wxx.proxy.dynamicproxy.handwriting.writeself.impl;

import com.test.wxx.proxy.dynamicproxy.handwriting.jdk.ProductService;
import com.test.wxx.proxy.dynamicproxy.handwriting.jdk.ProductServiceImpl;

public class Test {
    public static void main(String[] args) {
        ProductService productService = new ProductServiceImpl();
        ProductService proxy = (ProductService) new CustomInvocationHandler().getInstance(productService);
        proxy.addProduct("野兽");
    }
}
