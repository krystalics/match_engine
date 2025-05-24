package com.github.krystalics.anno;

import java.lang.annotation.*;

/**
 * @Author linjiabao001
 * @Date 2025/5/21
 * @Description https://dunwu.github.io/javacore/pages/ecc011/#retention
 */
@Documented //表示无论何时使用指定的注解，都应使用 Javadoc（默认情况下，注释不包含在 Javadoc 中）
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR}) //作用范围是个数组，这里包含方法，构造方法
@Retention(RetentionPolicy.RUNTIME) //生效阶段：source 源文件有效，编译器忽略；class 在class文件中有效，jvm忽略；runtime 运行时有效
public @interface TestAnno {
}
