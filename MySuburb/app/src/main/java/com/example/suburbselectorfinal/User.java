package com.example.suburbselectorfinal;

import java.util.List;

public class User {

    public String fullName, age, email,profilePic;

    public List<String> favourites;

    public User(){

    }

    public User(String fullName, String age, String email, List<String> favourites,String profilePic){
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.favourites = favourites;
        this.profilePic = profilePic;
    }
}
