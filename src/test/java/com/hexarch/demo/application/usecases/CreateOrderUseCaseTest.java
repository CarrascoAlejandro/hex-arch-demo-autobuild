package com.hexarch.demo.application.usecases;

import com.hexarch.demo.domain.model.*;
import com.hexarch.demo.domain.ports.CustomerRepository;
import com.hexarch.demo.domain.ports.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private CustomerRepository customerRepository;
    
    private CreateOrderUseCase createOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createOrderUseCase = new CreateOrderUseCase(orderRepository, customerRepository);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Given
        CustomerId customerId = CustomerId.generate();
        Customer customer = new Customer(customerId, "John Doe", "john@example.com");
        
        CreateOrderUseCase.CreateOrderItemCommand itemCommand = 
                new CreateOrderUseCase.CreateOrderItemCommand("Product 1", 10.0, "USD", 2);
        
        CreateOrderUseCase.CreateOrderCommand command = 
                new CreateOrderUseCase.CreateOrderCommand(customerId, List.of(itemCommand));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Order result = createOrderUseCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(OrderStatus.CREATED, result.getStatus());
        assertEquals(1, result.getItems().size());
        assertEquals(Money.of(20.0, "USD"), result.getTotalAmount());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Given
        CustomerId customerId = CustomerId.generate();
        CreateOrderUseCase.CreateOrderItemCommand itemCommand = 
                new CreateOrderUseCase.CreateOrderItemCommand("Product 1", 10.0, "USD", 2);
        
        CreateOrderUseCase.CreateOrderCommand command = 
                new CreateOrderUseCase.CreateOrderCommand(customerId, List.of(itemCommand));

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> createOrderUseCase.execute(command));
    }
}