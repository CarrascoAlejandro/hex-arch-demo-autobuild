package com.hexarch.demo.application.usecases;

import com.hexarch.demo.domain.model.Customer;
import com.hexarch.demo.domain.model.Order;
import com.hexarch.demo.domain.model.OrderId;
import com.hexarch.demo.domain.ports.CustomerRepository;
import com.hexarch.demo.domain.ports.EmailService;
import com.hexarch.demo.domain.ports.OrderRepository;
import com.hexarch.demo.domain.ports.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmOrderUseCase {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final PaymentService paymentService;
    private final EmailService emailService;

    public ConfirmOrderUseCase(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            PaymentService paymentService,
            EmailService emailService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.paymentService = paymentService;
        this.emailService = emailService;
    }

    @Transactional
    public Order execute(ConfirmOrderCommand command) {
        // Find the order
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + command.orderId()));

        // Find the customer
        Customer customer = customerRepository.findById(order.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + order.getCustomerId()));

        // Confirm the order
        order.confirm();
        Order savedOrder = orderRepository.save(order);

        // Process payment
        String paymentReference = "PAY-" + order.getId().toString();
        boolean paymentSuccessful = paymentService.processPayment(paymentReference, order.getTotalAmount());

        if (paymentSuccessful) {
            order.markAsPaid();
            savedOrder = orderRepository.save(order);

            // Send confirmation email
            String orderDetails = buildOrderDetails(order);
            emailService.sendOrderConfirmationEmail(
                    customer.getEmail(),
                    order.getId().toString(),
                    orderDetails
            );
        } else {
            throw new RuntimeException("Payment processing failed for order: " + order.getId());
        }

        return savedOrder;
    }

    private String buildOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Order ID: ").append(order.getId()).append("\n");
        details.append("Total Amount: ").append(order.getTotalAmount()).append("\n");
        details.append("Items:\n");
        
        order.getItems().forEach(item -> {
            details.append("- ").append(item.getProductName())
                    .append(" (Qty: ").append(item.getQuantity())
                    .append(", Price: ").append(item.getTotalPrice())
                    .append(")\n");
        });
        
        return details.toString();
    }

    public record ConfirmOrderCommand(OrderId orderId) {}
}