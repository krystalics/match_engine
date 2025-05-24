package com.github.krystalics.anno;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 用于处理自定义注解的代码，spring就是这样扫描查看文件中带有特定注解的，进行服务的构建
// 1。有类用注解
// 2。用反射获取注解的值、
// 3。对值进行check等操作
public class RegexValidUtil {
    public static boolean check(Object obj) throws Exception {
        boolean result = true;
        StringBuilder sb = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断成员是否被 @RegexValid 注解所修饰
            if (field.isAnnotationPresent(RegexValid.class)) {
                RegexValid annotation = field.getAnnotation(RegexValid.class);
                String value = annotation.value(); //获取这个注解上的value
                if (value.equals("")) { //
                    value = annotation.policy().getPolicy();
                }

                field.setAccessible(true); //访问私有成员
                Object fieldObj = null; //获取field的对象
                try {
                    fieldObj = field.get(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fieldObj == null) {
                    sb.append("\n")
                            .append(String.format("%s 类中的 %s 字段不能为空！", obj.getClass().getName(), field.getName()));
                    result = false;
                } else {
                    if (fieldObj instanceof String) {
                        String text = (String) fieldObj;
                        Pattern p = Pattern.compile(value);
                        Matcher m = p.matcher(text);
                        result = m.matches();
                        if (!result) {
                            sb.append("\n").append(String.format("%s 不是合法的 %s ！", text, field.getName()));
                        }
                    } else {
                        sb.append("\n").append(
                                String.format("%s 类中的 %s 字段不是字符串类型，不能使用此注解校验！", obj.getClass().getName(), field.getName()));
                        result = false;
                    }
                }
            }
        }

        if (sb.length() > 0) {
            throw new Exception(sb.toString());
        }
        return result;
    }
}
