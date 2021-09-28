package com.example.xermart.models;

public class Orders {
    String email;
    String phonenumber;
    String item;
    String quantity;
    String total;

    public Orders() {

    }

    public Orders(String email, String phonenumber, String item, String quantity, String total) {
        this.email = email;
        this.phonenumber = phonenumber;
        this.item = item;
        this.quantity = quantity;
        this.total = total;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}