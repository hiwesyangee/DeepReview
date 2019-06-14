package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 静态内置类。
 */
public class MyThread22 {
    static private String username;
    static private String password;

    static class PrivateClass {
        private String age;
        private String address;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void printPublicProperty() {
            System.out.println(username + " " + password);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


class Run22 {
    public static void main(String[] args) {
        MyThread22 outclass = new MyThread22();
        outclass.setUsername("suernameValue");
        outclass.setPassword("passwordValue");
        System.out.println(outclass.getUsername() + " " + outclass.getPassword());
        MyThread22.PrivateClass privateClass = new MyThread22.PrivateClass();
        privateClass.setAge("ageValue");
        privateClass.setAddress("addressValue");
        System.out.println(privateClass.getAge() + " " + privateClass.getAddress());
    }
}