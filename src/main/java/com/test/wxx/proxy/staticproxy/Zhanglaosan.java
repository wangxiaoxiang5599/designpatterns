package com.test.wxx.proxy.staticproxy;

public class Zhanglaosan implements IPerson{

    private Zhangsan zhangsan;

    public Zhanglaosan(Zhangsan zhangsan){
        this.zhangsan = zhangsan;
    }

    public void findLove() {
        System.out.println("开始物色...");
        zhangsan.findLove();
        System.out.println("开始交往...");
    }
}
