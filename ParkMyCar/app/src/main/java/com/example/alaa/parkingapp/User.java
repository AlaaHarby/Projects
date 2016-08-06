package com.example.alaa.parkingapp;

/**
 * Created by Alaa on 4/20/2016.
 */
public class User {

    private String name;
    private String email;
    private String password;

    public User () {
    }

    public User (String mName, String mEmail, String mPassword) {
        this.email = mEmail;
        this.name = mName;
        this.password = mPassword;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() { return password; }

}
