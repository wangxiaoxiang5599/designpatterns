package com.test.wxx.proxy.dynamicproxy.cglibproxy;

import com.test.wxx.proxy.dynamicproxy.jdkproxy.Zhangsan;

public class Test {
    public static void main(String[] args) {
        CglibMeipo cglibMeipo = new CglibMeipo();
        Zhangsan zhangsan = (Zhangsan) cglibMeipo.getInstance(Zhangsan.class);
        zhangsan.findLove();
    }
}
