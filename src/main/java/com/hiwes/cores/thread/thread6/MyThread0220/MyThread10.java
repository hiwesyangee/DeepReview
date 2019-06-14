package com.hiwes.cores.thread.thread6.MyThread0220;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 完善关于枚举enum的使用，构建单例模式:
 */
public class MyThread10 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(MyObject10.getConnection().hashCode());
        }
    }
}

class MyObject10 {
    public enum MyEnumSingleton {
        connectionFactory;
        private Connection connection;

        private MyEnumSingleton() {
            try {
                System.out.println("创建MyObject对象.");
                String url = "jdbc:sqlservce://localhost:1079;databaseName=y2";
                String username = "sa";
                String password = "";
                String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                Class.forName(driverName);
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.getErrorCode();
            }
        }

        public Connection getConnection() {
            return connection;
        }
    }

    public static Connection getConnection() {
        return MyEnumSingleton.connectionFactory.getConnection();
    }
}


class Run10 {
    public static void main(String[] args) {
        MyThread10 t1 = new MyThread10();
        MyThread10 t2 = new MyThread10();
        MyThread10 t3 = new MyThread10();
        t1.start();
        t2.start();
        t3.start();
    }
}