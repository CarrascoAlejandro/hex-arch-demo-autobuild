package com.hexarch.demo.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

class OrderTest {

    @Test
    void shouldCreateOrderWithItems() {
        // Given
        CustomerId customerId = CustomerId.generate();
        OrderId orderId = OrderId.generate();
        OrderItem item1 = new OrderItem("Product 1", Money.of(10.0, "USD"), 2);
        OrderItem item2 = new OrderItem("Product 2", Money.of(15.0, "USD"), 1);
        List<OrderItem> items = List.of(item1, item2);

        // When
        Order order = new Order(orderId, customerId, items);

        // Then
        assertEquals(orderId, order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(2, order.getItems().size());
        assertEquals(Money.of(35.0, "USD"), order.getTotalAmount());
        assertFalse(order.isConfirmed());
        assertFalse(order.isPaid());
    }

    @Test
    void shouldConfirmOrder() {
        // Given
        CustomerId customerId = CustomerId.generate();
        OrderId orderId = OrderId.generate();
        OrderItem item = new OrderItem("Product", Money.of(10.0, "USD"), 1);
        Order order = new Order(orderId, customerId, List.of(item));

        // When
        order.confirm();

        // Then
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertTrue(order.isConfirmed());
        assertFalse(order.isPaid());
        assertNotNull(order.getConfirmedAt());
    }

    @Test
    void shouldMarkOrderAsPaid() {
        // Given
        CustomerId customerId = CustomerId.generate();
        OrderId orderId = OrderId.generate();
        OrderItem item = new OrderItem("Product", Money.of(10.0, "USD"), 1);
        Order order = new Order(orderId, customerId, List.of(item));
        order.confirm();

        // When
        order.markAsPaid();

        // Then
        assertEquals(OrderStatus.PAID, order.getStatus());
        assertTrue(order.isConfirmed());
        assertTrue(order.isPaid());
    }

    @Test
    void shouldThrowExceptionWhenConfirmingNonCreatedOrder() {
        // Given
        CustomerId customerId = CustomerId.generate();
        OrderId orderId = OrderId.generate();
        OrderItem item = new OrderItem("Product", Money.of(10.0, "USD"), 1);
        Order order = new Order(orderId, customerId, List.of(item));
        order.confirm();

        // When & Then
        assertThrows(IllegalStateException.class, order::confirm);
    }

    @Test
    void shouldThrowExceptionWhenMarkingNonConfirmedOrderAsPaid() {
        // Given
        CustomerId customerId = CustomerId.generate();
        OrderId orderId = OrderId.generate();
        OrderItem item = new OrderItem("Product", Money.of(10.0, "USD"), 1);
        Order order = new Order(orderId, customerId, List.of(item));

        // When & Then
        assertThrows(IllegalStateException.class, order::markAsPaid);
    }

    @Test
    void shouldThrowExceptionWhenCreatingOrderWithoutItems() {
        // Given
        CustomerId customerId = CustomerId.generate();
        OrderId orderId = OrderId.generate();

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> new Order(orderId, customerId, List.of()));
    }
}