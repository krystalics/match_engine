package com.github.krystalics.utils;

import java.lang.reflect.Method;

/**
 * @Author linjiabao001
 * @Date 2025/5/21
 * @Description
 */
public class GenericTest extends HelloUtils{
//    private T value;
//
//    public T getValue() {
//        return value;
//    }

    public String getSS() {
        return "ss";
    }

    public static void main(String[] args) throws Exception {
        Class<?> test = Class.forName("com.github.krystalics.utils.GenericTest");
        Object o = test.newInstance();
        Method getSS = test.getMethod("getSS");
        if (o instanceof HelloUtils) { //instanceof判断的时候父类也可以
            System.out.println("instance judge pass");

        }

        System.out.println(getSS.invoke(o));
    }
}
