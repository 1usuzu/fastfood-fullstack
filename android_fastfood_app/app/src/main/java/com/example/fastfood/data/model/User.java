package com.example.fastfood.data.model;

public class User {
    private String _id;
    private String name;
    private String phone;

    // Constructors
    public User() {}

    public User(String _id, String name, String phone) {
        this._id = _id;
        this.name = name;
        this.phone = phone;
    }

    // Getters and setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
