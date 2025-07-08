package com.example.fastfood.data.model;

public class PhoneRequest {
    private String phone;

    // ✅ Bổ sung constructor có đối số
    public PhoneRequest(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
