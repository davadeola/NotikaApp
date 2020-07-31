package com.example.notika;

public class Menus {
    private final int profile_img;
    private String userName;


    public Menus(int profile_img, String userName) {
        this.profile_img = profile_img;
        this.userName = userName;

    }

    public int getProfile_img() {
        return profile_img;
    }

    public String getUserName() {
        return userName;
    }
}
