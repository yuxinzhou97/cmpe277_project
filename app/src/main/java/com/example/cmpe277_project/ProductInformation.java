package com.example.cmpe277_project;

public class ProductInformation {

    public String name;
    public String description;
    public String price;
    public String url;
    public ProductInformation(){}

    public ProductInformation(String name, String url, String description, String price) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.price = price;
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

}