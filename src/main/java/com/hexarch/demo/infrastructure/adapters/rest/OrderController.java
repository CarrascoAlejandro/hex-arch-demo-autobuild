package com.hexarch.demo.infrastructure.adapters.rest;

import com.hexarch.demo.application.usecases.ConfirmOrderUseCase;
import com.hexarch.demo.application.usecases.CreateOrderUseCase;
import com.hexarch.demo.application.usecases.GetOrderUseCase;
import com.hexarch.demo.domain.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                          ConfirmOrderUseCase confirmOrderUseCase,
                          GetOrderUseCase getOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            List<CreateOrderUseCase.CreateOrderItemCommand> itemCommands = request.items().stream()
                    .map(item -> new CreateOrderUseCase.CreateOrderItemCommand(
                            item.productName(),
                            item.unitPrice(),
                            item.currency(),
                            item.quantity()))
                    .toList();

            CreateOrderUseCase.CreateOrderCommand command = 
                    new CreateOrderUseCase.CreateOrderCommand(
                            CustomerId.fromString(request.customerId()),
                            itemCommands);

            Order order = createOrderUseCase.execute(command);
            OrderResponse response = toOrderResponse(order);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String orderId) {
        try {
            ConfirmOrderUseCase.ConfirmOrderCommand command = 
                    new ConfirmOrderUseCase.ConfirmOrderCommand(OrderId.fromString(orderId));

            Order order = confirmOrderUseCase.execute(command);
            OrderResponse response = toOrderResponse(order);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        try {
            GetOrderUseCase.GetOrderQuery query = 
                    new GetOrderUseCase.GetOrderQuery(OrderId.fromString(orderId));

            Order order = getOrderUseCase.execute(query);
            OrderResponse response = toOrderResponse(order);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProductName(),
                        item.getUnitPrice().getAmount().doubleValue(),
                        item.getUnitPrice().getCurrency(),
                        item.getQuantity(),
                        item.getTotalPrice().getAmount().doubleValue()))
                .toList();

        return new OrderResponse(
                order.getId().toString(),
                order.getCustomerId().toString(),
                order.getStatus().name(),
                order.getTotalAmount().getAmount().doubleValue(),
                order.getTotalAmount().getCurrency(),
                order.getCreatedAt(),
                order.getConfirmedAt(),
                itemResponses
        );
    }

    public record CreateOrderRequest(String customerId, List<CreateOrderItemRequest> items) {}
    
    public record CreateOrderItemRequest(String productName, double unitPrice, String currency, int quantity) {}
    
    public record OrderResponse(String id, String customerId, String status, double totalAmount, 
                               String currency, LocalDateTime createdAt, LocalDateTime confirmedAt,
                               List<OrderItemResponse> items) {}
    
    public record OrderItemResponse(String productName, double unitPrice, String currency, 
                                   int quantity, double totalPrice) {}
}