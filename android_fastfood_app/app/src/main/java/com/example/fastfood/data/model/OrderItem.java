package com.example.fastfood.data.model;

public class OrderItem {
    private String date;
    private String orderCode;
    private String items;
    private String total;
    private String status;

    public OrderItem(String date, String orderCode, String items, String total, String status) {
        this.date = date;
        this.orderCode = orderCode;
        this.items = items;
        this.total = total;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getItems() {
        return items;
    }

    public String getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }
}
