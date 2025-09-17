package com.hexarch.demo.application.usecases;

import com.hexarch.demo.domain.model.Order;
import com.hexarch.demo.domain.model.OrderId;
import com.hexarch.demo.domain.ports.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class GetOrderUseCase {
    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(GetOrderQuery query) {
        return orderRepository.findById(query.orderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + query.orderId()));
    }

    public record GetOrderQuery(OrderId orderId) {}
}