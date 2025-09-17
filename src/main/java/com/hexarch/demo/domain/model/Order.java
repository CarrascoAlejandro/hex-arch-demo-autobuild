package com.hexarch.demo.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime confirmedAt;

    public Order(OrderId id, CustomerId customerId, List<OrderItem> items) {
        this.id = Objects.requireNonNull(id, "Order ID cannot be null");
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.items = new ArrayList<>(Objects.requireNonNull(items, "Order items cannot be null"));
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
    }

    public void confirm() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Only created orders can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    public void markAsPaid() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed orders can be marked as paid");
        }
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw new IllegalStateException("Paid orders cannot be cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public Money getTotalAmount() {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Money.zero("USD"), Money::add);
    }

    public boolean isConfirmed() {
        return status == OrderStatus.CONFIRMED || status == OrderStatus.PAID;
    }

    public boolean isPaid() {
        return status == OrderStatus.PAID;
    }

    public OrderId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", status=" + status +
                ", totalAmount=" + getTotalAmount() +
                ", createdAt=" + createdAt +
                '}';
    }
}