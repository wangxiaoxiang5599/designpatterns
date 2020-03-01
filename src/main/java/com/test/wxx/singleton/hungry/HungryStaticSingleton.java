package com.test.wxx.singleton.hungry;

/**
 * 与hungryStaticSingleton无区别 写法装逼
 */
public class HungryStaticSingleton {

    private static final HungryStaticSingleton hungryStaticSingleton;

    static {
        hungryStaticSingleton = new HungryStaticSingleton();
    }

    private HungryStaticSingleton(){}

    public static HungryStaticSingleton getInstance(){
        return hungryStaticSingleton;
    }
}
