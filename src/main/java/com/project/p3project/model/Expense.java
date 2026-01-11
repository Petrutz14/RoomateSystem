package com.project.p3project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private Long id;
    private Long userId;
    private Long apartmentId;
    private String title;
    private String category; // New field
    private BigDecimal amount;
    @JsonProperty("date") //Map JSON to this
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expenseDate;
    private LocalDate createdAt;

    public Expense() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getApartmentId() { return apartmentId; }
    public void setApartmentId(Long apartmentId) { this.apartmentId = apartmentId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}