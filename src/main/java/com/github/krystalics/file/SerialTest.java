package com.github.krystalics.file;

import java.io.*;

/**
 * @Author linjiabao001
 * @Date 2025/5/21
 * @Description
 */
public class SerialTest {
    static class Person implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name = null;
        private Integer age = null;

        public Person() {
        }

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", age=" + age + "}'";
        }
    }

    //序列化对象到文件中
    private static void serialize(String fileName) throws IOException {
        File file = new File(fileName);
        OutputStream out = new FileOutputStream(file); // 文件输出流
        ObjectOutputStream oos = new ObjectOutputStream(out);

        oos.writeObject(new Person("Jack", 30));
        oos.close();
        out.close();
    }

    private static void deserialize(String fileName) throws Exception {
        File file = new File(fileName);
        FileInputStream in = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(in);

        Object o = ois.readObject();
        ois.close();
        in.close();
        System.out.println(o);
    }

    public static void main(String[] args) throws Exception {
        final String filename = "./text.dat";
        serialize(filename);
        deserialize(filename);
    }
}
