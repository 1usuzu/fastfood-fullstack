package com.example.fastfood.data.model;

public class UserLoginRequest {
    private String phone;
    private String password;

    public UserLoginRequest(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    // Getter & Setter nếu cần
}
