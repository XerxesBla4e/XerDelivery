package com.example.xermart;

public class Orders {


    String name;
    String email;
    String number;
    String item;


    public Orders(String name, String email, String number, String item) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getItem() {
        return item;
    }
}
