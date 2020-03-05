package com.test.wxx.proxy.dynamicproxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkMeipo implements InvocationHandler {

    private IPerson target;

    public IPerson getInstance(IPerson target){
        this.target = target;
        Class clazz = target.getClass();
        return (IPerson) Proxy.newProxyInstance(clazz.getClassLoader(),
                clazz.getInterfaces(), this);
    }



    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(this.target, args);
        after();
        return result;
    }

    private void before() {
        System.out.println("before()");
    }

    private void after() {
        System.out.println("after()");
    }
}
