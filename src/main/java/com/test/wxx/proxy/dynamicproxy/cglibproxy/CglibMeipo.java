package com.test.wxx.proxy.dynamicproxy.cglibproxy;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Cglib 继承的方式，覆盖父类的方法
 * JDK采用的实现的方式，必须要求代理的目标对象一定要实现一个接口
 * 理想：都是通过生成字节码，重组一个新的类
 *
 * JDK Proxy 对于用户而言，依赖更强，调用也更复杂
 * Cglib 对目标类没有任何的要求
 *
 * Cglib 效率更高，性能也更高，底层没有用到反射
 * JDK Proxy 生成逻辑较为简单，执行效率更低，每次都要用反射
 *
 * Cglib 有个坑，目标代理类不能有final的方法，忽略final修饰的方法
 */
import java.lang.reflect.Method;

/**
 * cglib代理 不需要代理的对象定义接口
 */
public class CglibMeipo implements MethodInterceptor {

    public Object getInstance(Class clazz){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
            throws Throwable {
        before();
        Object result = methodProxy.invokeSuper(o, objects);
        after();
        return result;
    }

    private void before() {
        System.out.println("开始物色美女。。。");
    }

    private void after() {
        System.out.println("互相看中，开始交往。。。");
    }
}
