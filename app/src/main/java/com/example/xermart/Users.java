package com.example.xermart;

public class Users {

    public String fullname, email, phonenumber;

    public Users() {

    }

    public Users(String fullname, String email, String phonenumber) {
        this.fullname = fullname;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
