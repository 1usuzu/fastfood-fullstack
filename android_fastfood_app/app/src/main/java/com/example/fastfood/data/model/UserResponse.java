package com.example.fastfood.data.model;

public class UserResponse {
    private String message;
    private String token;
    private User user;  // Tùy backend có trả về thông tin user không

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    // Inner class hoặc bạn có thể tạo file riêng User.java
    public static class User {
        private String _id;
        private String phone;

        public String getId() {
            return _id;
        }

        public String getPhone() {
            return phone;
        }
    }
}
