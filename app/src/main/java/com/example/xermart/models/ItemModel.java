package com.example.xermart.models;

public class ItemModel {
    String ItemName;
    String Price;
    String image;
    String vendorName;
    String market;

    public ItemModel() {
    }

    public ItemModel(String itemName, String price, String image, String vendorName, String market) {
        ItemName = itemName;
        Price = price;
        this.image = image;
        this.vendorName = vendorName;
        this.market = market;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
}
