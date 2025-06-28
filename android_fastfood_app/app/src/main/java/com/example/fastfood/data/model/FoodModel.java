package com.example.fastfood.data.model;

import com.google.gson.annotations.SerializedName;

public class FoodModel {

    @SerializedName("_id")
    private String id;

    private String name;
    private double price;
    private String imageUrl;
    private String description;

    public FoodModel() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}