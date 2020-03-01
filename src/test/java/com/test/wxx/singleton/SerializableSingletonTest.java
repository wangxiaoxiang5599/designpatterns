package com.test.wxx.singleton;

import com.test.wxx.singleton.serializable.SerializableSingleton;

import java.io.*;

public class SerializableSingletonTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SerializableSingleton s1 = SerializableSingleton.getInstance();

        FileOutputStream fos =  new FileOutputStream("SerializableSingleton.obj");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(s1);
        oos.flush();
        oos.close();

        SerializableSingleton s2 = null;
        FileInputStream fis = new FileInputStream("SerializableSingleton.obj");
        ObjectInputStream ois = new ObjectInputStream(fis);
        s2 = (SerializableSingleton) ois.readObject();

        System.out.println(s1);
        System.out.println(s2);
    }
}
