package com.hexarch.demo.application.usecases;

import com.hexarch.demo.domain.model.Customer;
import com.hexarch.demo.domain.model.CustomerId;
import com.hexarch.demo.domain.ports.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerUseCase {
    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer execute(CreateCustomerCommand command) {
        Customer customer = new Customer(
                CustomerId.generate(),
                command.name(),
                command.email()
        );
        return customerRepository.save(customer);
    }

    public record CreateCustomerCommand(
            String name,
            String email
    ) {}
}