package com.test.wxx.proxy.general;

public class Proxy {

    private ISubject subject;

    public Proxy(ISubject subject){
        this.subject = subject;
    }

    public void request(){
        before();
        subject.request();
        after();
    }

    private void after() {
        System.out.println("called after request()");
    }

    private void before() {
        System.out.println("called before request()");
    }




}
