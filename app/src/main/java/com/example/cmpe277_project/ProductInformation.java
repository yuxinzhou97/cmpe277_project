package com.example.cmpe277_project;

public class ProductInformation {

    public String name;
    public String description;
    public String price;
    public String url;
    public String userID;
    public ProductInformation(){}

    public ProductInformation(String name, String url, String description, String price, String userID) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.price = price;
        this.userID = userID;
    }

    public String getDescription() {
        return description;
    }


    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public String getUserID() {
        return userID;
    }

}