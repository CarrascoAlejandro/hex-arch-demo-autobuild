package com.hexarch.demo.application.usecases;

import com.hexarch.demo.domain.model.*;
import com.hexarch.demo.domain.ports.CustomerRepository;
import com.hexarch.demo.domain.ports.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateOrderUseCase {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public CreateOrderUseCase(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public Order execute(CreateOrderCommand command) {
        // Validate customer exists
        Customer customer = customerRepository.findById(command.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + command.customerId()));

        // Create order items
        List<OrderItem> orderItems = command.items().stream()
                .map(item -> new OrderItem(
                        item.productName(),
                        Money.of(item.unitPrice(), item.currency()),
                        item.quantity()))
                .toList();

        // Create and save order
        Order order = new Order(OrderId.generate(), command.customerId(), orderItems);
        return orderRepository.save(order);
    }

    public record CreateOrderCommand(
            CustomerId customerId,
            List<CreateOrderItemCommand> items
    ) {}

    public record CreateOrderItemCommand(
            String productName,
            double unitPrice,
            String currency,
            int quantity
    ) {}
}