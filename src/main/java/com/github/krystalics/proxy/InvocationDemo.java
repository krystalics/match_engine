package com.github.krystalics.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author linjiabao001
 * @Date 2025/5/21
 * @Description 代理的作用就是在不修改原来类的基础上修改功能、动态代理就是可以进行更全面的操纵，一个方法代理对象所有的方法
 * 然后执行的时候还是按照原对象执行，只不过这个对象需要是Proxy.newInstance()生成的，
 * 其他的都是和原对象相同
 * 优点：相对于静态代理模式，不需要硬编码接口，代码复用率高。
 *
 * 缺点：强制要求代理类实现 InvocationHandler 接口。
 *
 * CGLIB 动态代理的工作步骤：
 *
 * 生成代理类的二进制字节码文件；
 * 加载二进制字节码，生成 Class 对象( 例如使用 Class.forName() 方法 )；
 * 通过反射机制获得实例构造，并创建代理类对象。
 * CGLIB 动态代理特点：
 *
 * 优点：使用字节码增强，比 JDK 动态代理方式性能高。可以在运行时对类或者是接口进行增强操作，且委托类无需实现接口。
 *
 * 缺点：不能对 final 类以及 final 方法进行代理。
 */
public class InvocationDemo implements InvocationHandler {

    private Object subject;
    //将要代理的对象初始化
    public InvocationDemo(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(proxy.getClass()); //class com.sun.proxy.$Proxy0
        // 在代理真实对象前我们可以添加一些自己的操作
        System.out.println("Before method");
        System.out.println("Call Method: " + method);

        Object obj = method.invoke(subject, args);

        // 在代理真实对象后我们也可以添加一些自己的操作
        System.out.println("After method");
        System.out.println(obj);
        return obj;
    }
}
