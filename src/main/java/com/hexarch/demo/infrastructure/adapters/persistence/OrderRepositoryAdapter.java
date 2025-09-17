package com.hexarch.demo.infrastructure.adapters.persistence;

import com.hexarch.demo.domain.model.*;
import com.hexarch.demo.domain.ports.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderRepositoryAdapter implements OrderRepository {
    private final SpringDataOrderRepository springDataOrderRepository;
    private final SpringDataCustomerRepository springDataCustomerRepository;

    public OrderRepositoryAdapter(SpringDataOrderRepository springDataOrderRepository,
                                SpringDataCustomerRepository springDataCustomerRepository) {
        this.springDataOrderRepository = springDataOrderRepository;
        this.springDataCustomerRepository = springDataCustomerRepository;
    }

    @Override
    public Order save(Order order) {
        CustomerEntity customerEntity = springDataCustomerRepository.findById(order.getCustomerId().toString())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + order.getCustomerId()));

        OrderEntity entity = toEntity(order, customerEntity);
        OrderEntity savedEntity = springDataOrderRepository.save(entity);
        
        // Save order items
        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(item -> toItemEntity(item, savedEntity))
                .toList();
        savedEntity.setItems(itemEntities);
        
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        OrderEntity entity = springDataOrderRepository.findByIdWithItems(orderId.toString());
        return entity != null ? Optional.of(toDomain(entity)) : Optional.empty();
    }

    private OrderEntity toEntity(Order order, CustomerEntity customerEntity) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId().toString());
        entity.setCustomer(customerEntity);
        entity.setStatus(OrderStatusEntity.valueOf(order.getStatus().name()));
        entity.setTotalAmount(order.getTotalAmount().getAmount());
        entity.setCurrency(order.getTotalAmount().getCurrency());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setConfirmedAt(order.getConfirmedAt());
        return entity;
    }

    private OrderItemEntity toItemEntity(OrderItem item, OrderEntity orderEntity) {
        return new OrderItemEntity(
                orderEntity,
                item.getProductName(),
                item.getUnitPrice().getAmount(),
                item.getUnitPrice().getCurrency(),
                item.getQuantity()
        );
    }

    private Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems() != null ? 
                entity.getItems().stream()
                        .map(this::toItemDomain)
                        .toList() : new ArrayList<>();

        Order order = new Order(
                OrderId.fromString(entity.getId()),
                CustomerId.fromString(entity.getCustomer().getId()),
                items
        );

        // Set status by simulating the state transitions
        if (entity.getStatus() == OrderStatusEntity.CONFIRMED) {
            order.confirm();
        } else if (entity.getStatus() == OrderStatusEntity.PAID) {
            order.confirm();
            order.markAsPaid();
        } else if (entity.getStatus() == OrderStatusEntity.CANCELLED) {
            order.cancel();
        }

        return order;
    }

    private OrderItem toItemDomain(OrderItemEntity entity) {
        Money unitPrice = new Money(entity.getUnitPrice(), entity.getCurrency());
        return new OrderItem(entity.getProductName(), unitPrice, entity.getQuantity());
    }
}