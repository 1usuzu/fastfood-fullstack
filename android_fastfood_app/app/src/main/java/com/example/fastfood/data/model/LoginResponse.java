package com.example.fastfood.data.model;

public class LoginResponse {
    private String message;
    private String token;
    private UserModel user;

    public String getToken() {
        return token;
    }
    public String getMessage() {
        return message;
    }
    public UserModel getUser() {
        return user;
    }
}