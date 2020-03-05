package com.test.wxx.proxy.dynamicproxy.handwriting.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1.拿到被代理对象的引用，然后获取他的接口 (Proxy.getInstance方法)
 * 2.JDK代理重新生成一个类，同时实现我们给的代理对象所实现的接口 （上面的反编译文件中实现了同样的接口）
 * 3.把被代理对象的引用拿到了（上面被代理对象中在静态代码块中通过反射获取到的信息，以及我们实现的JdkInvocationHandler中的target）
 * 4.重新动态生成一个class字节码
 * 5.然后编译
 */
public class JdkInvocationHandler implements InvocationHandler {

    private ProductService target;

    public ProductService getInstance(ProductService target){
        this.target = target;
        Class clazz = this.target.getClass();
        return (ProductService) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(new Date());
        System.out.println("日期【" + currentDate + "】添加了一款产品........");
        Object object = method.invoke(this.target, args);
        System.out.println("添加产品结束.......");
        return object;
    }
}
