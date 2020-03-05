package com.test.wxx.proxy.general;

public class RealSubject implements ISubject {
    public void request() {
        System.out.println("Real service is called!");
    }
}
