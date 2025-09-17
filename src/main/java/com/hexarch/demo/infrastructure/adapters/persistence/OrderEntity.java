package com.hexarch.demo.infrastructure.adapters.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatusEntity status;
    
    @Column(name = "total_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    
    @Column(nullable = false)
    private String currency;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> items;

    public OrderEntity() {}

    public OrderEntity(String id, CustomerEntity customer, OrderStatusEntity status, 
                      BigDecimal totalAmount, String currency, LocalDateTime createdAt) {
        this.id = id;
        this.customer = customer;
        this.status = status;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public OrderStatusEntity getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEntity status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public List<OrderItemEntity> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEntity> items) {
        this.items = items;
    }
}