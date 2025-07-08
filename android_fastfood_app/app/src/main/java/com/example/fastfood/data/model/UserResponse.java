package com.example.fastfood.data.model;

public class UserResponse {
    private boolean success;
    private String message;
    private String token; // nếu backend có trả về token

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
