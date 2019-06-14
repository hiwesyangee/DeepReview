package com.hiwes.cores.thread.thread6.MyThread0220;

import java.io.*;

/**
 * 序列化与反序列化的单例模式实现。
 */
public class MyThread08 {
}


class MyObject08 implements Serializable {

    private static final long serialVersionUID = 888L;

    // 内部类方式
    private static class MyObjectHandler {
        private static final MyObject08 myObject = new MyObject08();
    }

    private MyObject08() {
    }

    public static MyObject08 getInstance() {
        return MyObjectHandler.myObject;
    }

    protected Object readResolve() throws ObjectStreamException {
        System.out.println("调用了readResolve方法.");
        return MyObjectHandler.myObject;
    }

}


class SaveAndRead {
    public static void main(String[] args) {
        try {
            MyObject08 myObject = MyObject08.getInstance();
            FileOutputStream fosRef = new FileOutputStream(new File("myObjectFile.txt"));
            ObjectOutputStream oosRef = new ObjectOutputStream(fosRef);

            oosRef.writeObject(myObject);
            oosRef.close();
            fosRef.close();
            System.out.println(myObject.hashCode());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fisRef = new FileInputStream(new File("myObjectFile.txt"));
            ObjectInputStream iosRef = new ObjectInputStream(fisRef);
            MyObject08 myObject = (MyObject08) iosRef.readObject();
            iosRef.close();
            fisRef.close();
            System.out.println(myObject.hashCode());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}