package com.hexarch.demo.infrastructure.adapters.rest;

import com.hexarch.demo.application.usecases.CreateCustomerUseCase;
import com.hexarch.demo.domain.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CreateCustomerUseCase createCustomerUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CreateCustomerRequest request) {
        try {
            CreateCustomerUseCase.CreateCustomerCommand command = 
                    new CreateCustomerUseCase.CreateCustomerCommand(request.name(), request.email());
            
            Customer customer = createCustomerUseCase.execute(command);
            
            CustomerResponse response = new CustomerResponse(
                    customer.getId().toString(),
                    customer.getName(),
                    customer.getEmail()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public record CreateCustomerRequest(String name, String email) {}
    
    public record CustomerResponse(String id, String name, String email) {}
}