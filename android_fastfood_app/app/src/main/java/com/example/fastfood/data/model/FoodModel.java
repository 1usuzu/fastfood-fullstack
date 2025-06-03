package com.example.fastfood.data.model;

import com.google.gson.annotations.SerializedName;

public class FoodModel {
    // Sử dụng @SerializedName nếu tên biến trong Java khác với tên trường trong JSON từ API
    @SerializedName("_id") // MongoDB thường dùng _id
    private String id;

    private String name;
    private double price;
    private String imageUrl;

    // Các trường timestamps nếu backend có trả về
    private String createdAt;
    private String updatedAt;


    public FoodModel() {} // Bắt buộc để Retrofit (Gson) chuyển JSON

    // Getters
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    // Setters (Có thể không cần thiết nếu bạn chỉ nhận dữ liệu từ API và không sửa đổi trực tiếp)
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

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}