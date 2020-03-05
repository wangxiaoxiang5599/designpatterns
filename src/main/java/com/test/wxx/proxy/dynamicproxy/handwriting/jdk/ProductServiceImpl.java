package com.test.wxx.proxy.dynamicproxy.handwriting.jdk;

public class ProductServiceImpl implements ProductService {

    public void addProduct(String productName) {
        System.out.println("正在添加产品：" + productName);
    }
}
