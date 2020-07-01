package com.example.notika.services.models;

public class User {

    private String email;
    private String username;
    private  String password;
    private String confirmPassword;

    public User (String username, String email, String password, String confirmPassword){
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }


}
