package com.hexarch.demo.domain.ports;

import com.hexarch.demo.domain.model.Customer;
import com.hexarch.demo.domain.model.CustomerId;
import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(CustomerId customerId);
}