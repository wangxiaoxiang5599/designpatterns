package com.test.wxx.proxy.dynamicproxy.jdkproxy;

public class test {
    public static void main(String[] args) {
        JdkMeipo jdkMeipo = new JdkMeipo();
        IPerson person = jdkMeipo.getInstance(new Zhaoliu());
        person.findLove();

        IPerson person1 = jdkMeipo.getInstance(new Zhangsan());
        person1.findLove();
    }
}
