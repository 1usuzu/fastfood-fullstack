package com.example.fastfood.data.model;

public class SendOtpRequest {
    private String phone;

    public SendOtpRequest(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
