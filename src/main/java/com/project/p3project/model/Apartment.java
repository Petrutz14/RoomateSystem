package com.project.p3project.model;

import java.time.LocalDateTime;

public class Apartment {
    private Long id;
    private String address;
    private LocalDateTime createdAt;

    public Apartment(Long id, String address, LocalDateTime createdAt) {
        this.id = id;
        this.address = address;
        this.createdAt = createdAt;
    }

    public Apartment() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}