package com.jady.sample.bean;

/**
 * Created by lipingfa on 2017/6/19.
 */
public class UserForLogin {
    private String name;
    private String password;

    public UserForLogin(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserForLogin{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
