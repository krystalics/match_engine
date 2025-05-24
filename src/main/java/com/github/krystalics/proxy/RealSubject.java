package com.github.krystalics.proxy;

/**
 * @Author linjiabao001
 * @Date 2025/5/21
 * @Description
 */
public class RealSubject extends SubjectFa implements Subject {
    public RealSubject() {
        super("str");
        System.out.println("real init");
    }

    @Override
    public void Hello(String say) {
        System.out.println("hello " + say);
    }

    @Override
    public String bye() {
        System.out.println("bye");
        return "bye";
    }
}
