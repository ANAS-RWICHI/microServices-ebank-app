package com._38_.customerservice.services;

import com._38_.customerservice.Entities.Customer;
import com._38_.customerservice.Repository.CustomerRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService (CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @McpTool(name = "get_all_customers",description = "Get all customers from the database")
    public List <Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @McpTool(name = "find_customer_by_id",description = "Find a customer by their ID")
     public Customer findCustomerById(@McpToolParam(description = "The ID of the customer to find") Long id) {
        return customerRepository.findById(id).orElseThrow(()->new RuntimeException("customer not found"));
     }

    @McpTool(name = "save_customer",description = "Save a customer to the database")
     public Customer saveCustomer (@McpToolParam(description = "The customer to save (name and email)") Customer customerInput) {
        Customer customer = Customer.builder()
                .name(customerInput.getName())
                .email(customerInput.getEmail())
                .build();

        return customerRepository.save(customer);
     }


}
