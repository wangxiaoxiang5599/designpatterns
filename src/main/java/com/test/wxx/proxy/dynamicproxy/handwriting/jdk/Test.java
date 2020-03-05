package com.test.wxx.proxy.dynamicproxy.handwriting.jdk;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;

public class Test {
    public static void main(String[] args) {
        ProductService productService = new JdkInvocationHandler().getInstance(new ProductServiceImpl());
        productService.addProduct("美女");

        // 将jdk生成的代理类输出字节码
        byte[] bytes = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{productService.getClass()});
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("$Proxy0.class");
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
