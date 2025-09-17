package com.hexarch.demo.infrastructure.adapters.persistence;

import com.hexarch.demo.domain.model.Customer;
import com.hexarch.demo.domain.model.CustomerId;
import com.hexarch.demo.domain.ports.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerRepositoryAdapter implements CustomerRepository {
    private final SpringDataCustomerRepository springDataCustomerRepository;

    public CustomerRepositoryAdapter(SpringDataCustomerRepository springDataCustomerRepository) {
        this.springDataCustomerRepository = springDataCustomerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = toEntity(customer);
        CustomerEntity savedEntity = springDataCustomerRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findById(CustomerId customerId) {
        return springDataCustomerRepository.findById(customerId.toString())
                .map(this::toDomain);
    }

    private CustomerEntity toEntity(Customer customer) {
        return new CustomerEntity(
                customer.getId().toString(),
                customer.getName(),
                customer.getEmail()
        );
    }

    private Customer toDomain(CustomerEntity entity) {
        return new Customer(
                CustomerId.fromString(entity.getId()),
                entity.getName(),
                entity.getEmail()
        );
    }
}