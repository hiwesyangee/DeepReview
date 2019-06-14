package com.hiwes.cores.thread.thread2.Thread0215;

/**
 * 内置类:
 */
public class MyThread21 {

    private String username;
    private String password;

    class PrivateClass {
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


class Run21 {
    public static void main(String[] args) {
        MyThread21 outclass = new MyThread21();
        outclass.setUsername("usernameValue");
        outclass.setPassword("passwordValue");
        System.out.println(outclass.getUsername() + " " + outclass.getPassword());
        MyThread21.PrivateClass privateClass = outclass.new PrivateClass();
        privateClass.setAge("ageValue");
        privateClass.setAddress("addressValue");
        System.out.println(privateClass.getAge() + " " + privateClass.getAddress());
    }
}