package com.example.xermart.models;

public class CartModel {
    int ID;
    String itemname;
    int quantity;
    int total;
    String image;

    public CartModel() {

    }

    public CartModel(int ID, String itemname, int quantity, int total, String image) {
        this.ID = ID;
        this.itemname = itemname;
        this.quantity = quantity;
        this.total = total;
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}