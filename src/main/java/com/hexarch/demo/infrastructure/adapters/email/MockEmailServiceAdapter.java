package com.hexarch.demo.infrastructure.adapters.email;

import com.hexarch.demo.domain.ports.EmailService;
import org.springframework.stereotype.Component;

@Component
public class MockEmailServiceAdapter implements EmailService {

    @Override
    public void sendOrderConfirmationEmail(String customerEmail, String orderId, String orderDetails) {
        // Simulate email sending
        System.out.println("Sending email to: " + customerEmail);
        System.out.println("Subject: Order Confirmation - " + orderId);
        System.out.println("Body:");
        System.out.println("Dear Customer,");
        System.out.println("Your order has been confirmed!");
        System.out.println(orderDetails);
        System.out.println("Thank you for your business.");
        System.out.println("---");
    }
}