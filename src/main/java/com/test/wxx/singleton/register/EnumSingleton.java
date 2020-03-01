package com.test.wxx.singleton.register;

/**
 * 枚举式单例(注册式)
 * 与饿汉式一样会有内存浪费情况
 */
public enum  EnumSingleton {
    INSTANCE;

    private Object data;

    public Object getData(){
        return data;
    }

    public void setData(Object data){
        this.data = data;
    }

    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
}
