package com.example.coen268project.Model;

import java.io.Serializable;
import java.util.HashMap;

public class AccountDao implements Serializable {
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;

    public AccountDao(String userName, String email, String phoneNumber, String password)
    {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = password;
    }

    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userName", getUserName());
        map.put("email", getEmail());
        map.put("phoneNumber", getPhoneNumber());
        map.put("password", getPassword());
        return map;
    }
}
