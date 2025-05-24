package com.github.krystalics.file;

import java.io.*;

/**
 * @Author linjiabao001
 * @Date 2025/5/21
 * @Description
 */
public class FileTest {
    public final static String filePath = "./tmp.log";

    public static void main(String[] args) throws Exception{
        write();
        read(filePath);
    }

    public static void write() throws Exception {
        File file = new File(filePath); //文件
        if (!file.exists()) {
            boolean create = file.createNewFile();
            if (create) {
                System.out.println("创建文件成功");
            } else {
                System.out.println("创建文件失败");
            }
        }

        FileOutputStream output = new FileOutputStream(file); //输出流
        String str = "Hello World\n";
        byte[] bytes = str.getBytes();
        output.write(bytes);

        // 第4步、关闭输出流
        output.close();
    }

    public static void read(String filepath) throws IOException {
        // 第1步、使用File类找到一个文件
        File f = new File(filepath);

        // 第2步、通过子类实例化父类对象
        InputStream input = new FileInputStream(f);

        // 第3步、进行读操作
        // 有三种读取方式，体会其差异
        byte[] bytes = new byte[(int) f.length()];
        int len = input.read(bytes); // 读取内容
        System.out.println("读入数据的长度：" + len);

        // 第4步、关闭输入流
        input.close();
        System.out.println("内容为：\n" + new String(bytes));
    }
}
