package com._38_.customerservice.Controllers;

import com._38_.customerservice.Entities.Customer;
import com._38_.customerservice.services.CustomerService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerRestController {
    private CustomerService customerService;

    public CustomerRestController (CustomerService customerService) {
        this.customerService = customerService;
    }

    private List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(()->new RuntimeException("customer not found"));
    }

    public Customer saveCustomer (Customer customer) {
        return customerRepository.save(customer);
    }
}
