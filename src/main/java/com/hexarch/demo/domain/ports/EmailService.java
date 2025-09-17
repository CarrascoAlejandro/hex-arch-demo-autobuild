package com.hexarch.demo.domain.ports;

public interface EmailService {
    void sendOrderConfirmationEmail(String customerEmail, String orderId, String orderDetails);
}