package com.mojaafar.tutorapp.Models;

public class Tutor {
    private String Email;
    private String fullname;
    private String phone;
    private String imageurl;
    private String role;
    private String bio;
    private String rating;
    private String uid;
    private String price;
    private String subject;


    public Tutor(String email, String fullname, String phone, String imageurl, String role, String bio, String rating, String uid, String price, String subject) {
        Email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.imageurl = imageurl;
        this.role = role;
        this.bio = bio;
        this.rating = rating;
        this.uid = uid;
        this.price = price;
        this.subject = subject;
    }

    public Tutor() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
