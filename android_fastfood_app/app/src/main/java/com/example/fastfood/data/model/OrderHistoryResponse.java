package com.example.fastfood.data.model;

import java.util.List;

public class OrderHistoryResponse {
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
