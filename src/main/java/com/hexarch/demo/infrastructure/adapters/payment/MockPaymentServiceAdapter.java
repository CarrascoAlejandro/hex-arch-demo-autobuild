package com.hexarch.demo.infrastructure.adapters.payment;

import com.hexarch.demo.domain.model.Money;
import com.hexarch.demo.domain.ports.PaymentService;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MockPaymentServiceAdapter implements PaymentService {
    private final Random random = new Random();

    @Override
    public boolean processPayment(String paymentReference, Money amount) {
        // Simulate payment processing delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        // Simulate 95% success rate
        boolean success = random.nextDouble() < 0.95;
        
        System.out.println("Processing payment: " + paymentReference + 
                          " for amount: " + amount + 
                          " - Result: " + (success ? "SUCCESS" : "FAILED"));
        
        return success;
    }
}