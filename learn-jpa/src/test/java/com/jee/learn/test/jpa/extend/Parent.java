package com.jee.learn.test.jpa.extend;

public abstract class Parent {

    protected abstract String say(String str);

    protected String hello(String str) {
        return str;
    }

}
