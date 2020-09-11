package com.mojaafar.tutorapp.Models;

public class User {
    private String Email;
    private String fullname;
    private String phone;
    private String imageurl;
    private String role;
    private String uid;


    public User(String email, String fullname, String phone, String imageurl, String role, String uid) {
        Email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.imageurl = imageurl;
        this.role = role;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
