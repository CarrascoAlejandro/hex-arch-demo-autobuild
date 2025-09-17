package com.hexarch.demo.domain.ports;

import com.hexarch.demo.domain.model.Money;

public interface PaymentService {
    boolean processPayment(String paymentReference, Money amount);
}