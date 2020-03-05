package com.test.wxx.proxy.staticproxy;

public class test {
    public static void main(String[] args) {
        Zhanglaosan zhanglaosan = new Zhanglaosan(new Zhangsan());
        zhanglaosan.findLove();
    }
}
