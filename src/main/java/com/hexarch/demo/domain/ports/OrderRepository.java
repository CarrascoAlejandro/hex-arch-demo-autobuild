package com.hexarch.demo.domain.ports;

import com.hexarch.demo.domain.model.Order;
import com.hexarch.demo.domain.model.OrderId;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(OrderId orderId);
}