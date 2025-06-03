package com.example.fastfood;

public class FoodModel {
    private String name;
    private double price;
    private String imageUrl;

    public FoodModel() {} // Bắt buộc để Retrofit chuyển JSON

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
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
}
