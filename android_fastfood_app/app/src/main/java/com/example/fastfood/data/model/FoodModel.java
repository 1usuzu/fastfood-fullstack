package com.example.fastfood.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class FoodModel implements Serializable {

    @SerializedName("_id")
    private String id;

    private String name;
    private double price;
    private String imageUrl;
    private String category;
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

    public String getCategory() {
        return category;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
