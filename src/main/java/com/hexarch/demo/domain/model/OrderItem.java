package com.hexarch.demo.domain.model;

import java.util.Objects;

public class OrderItem {
    private final String productName;
    private final Money unitPrice;
    private final int quantity;

    public OrderItem(String productName, Money unitPrice, int quantity) {
        this.productName = Objects.requireNonNull(productName, "Product name cannot be null");
        this.unitPrice = Objects.requireNonNull(unitPrice, "Unit price cannot be null");
        this.quantity = quantity;

        if (productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    public Money getTotalPrice() {
        return new Money(
            unitPrice.getAmount().multiply(java.math.BigDecimal.valueOf(quantity)),
            unitPrice.getCurrency()
        );
    }

    public String getProductName() {
        return productName;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                Objects.equals(productName, orderItem.productName) &&
                Objects.equals(unitPrice, orderItem.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, unitPrice, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productName='" + productName + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
}