package com.hexarch.demo.infrastructure.adapters.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderManagementIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Test
    void shouldCompleteOrderManagementFlow() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create a customer first
        CustomerController.CreateCustomerRequest customerRequest = 
                new CustomerController.CreateCustomerRequest("John Doe", "john@example.com");

        String customerResponse = mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        CustomerController.CustomerResponse customer = 
                objectMapper.readValue(customerResponse, CustomerController.CustomerResponse.class);

        // Create an order
        OrderController.CreateOrderItemRequest item = 
                new OrderController.CreateOrderItemRequest("Test Product", 10.0, "USD", 2);
        OrderController.CreateOrderRequest orderRequest = 
                new OrderController.CreateOrderRequest(customer.id(), List.of(item));

        String orderResponse = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(customer.id()))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.totalAmount").value(20.0))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].productName").value("Test Product"))
                .andReturn().getResponse().getContentAsString();

        OrderController.OrderResponse order = 
                objectMapper.readValue(orderResponse, OrderController.OrderResponse.class);

        // Get the order
        mockMvc.perform(get("/api/orders/" + order.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.id()))
                .andExpect(jsonPath("$.status").value("CREATED"));

        // Confirm the order
        mockMvc.perform(post("/api/orders/" + order.id() + "/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.confirmedAt").exists());
    }
}