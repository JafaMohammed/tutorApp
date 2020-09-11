package com.mojaafar.tutorapp.http;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phonenumber")
        @Expose
        private int phonenumber;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("option")
        @Expose
        private String option;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("passwordConfirm")
        @Expose
        private String passwordConfirm;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(int phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPasswordConfirm() {
            return passwordConfirm;
        }

        public void setPasswordConfirm(String passwordConfirm) {
            this.passwordConfirm = passwordConfirm;
        }

    }
