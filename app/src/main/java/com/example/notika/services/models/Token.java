package com.example.notika.services.models;

public class Token {
    private String token, email, imageUrl, username;

    //create a sharedPreference file for saving the email, the imageurl and the username


    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;

        //save
    }
}
