package com.example.fastfood.data.model;

public class Order {

    private int id;
    private String createdAt;
    private String status;
    private int total;

    // 👇 Đây là field bạn cần thêm để hiển thị "Số món"
    private int itemCount;

    // Getter và Setter
    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public int getTotal() {
        return total;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    // Nếu cần setter các trường khác
    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
