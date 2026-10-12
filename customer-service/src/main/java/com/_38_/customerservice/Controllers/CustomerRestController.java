package com._38_.customerservice.Controllers;

import com._38_.customerservice.Entities.Customer;
import com._38_.customerservice.services.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerRestController {
    private final CustomerService customerService;

    public CustomerRestController (CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    @GetMapping("/customers/{id}")
    public Customer findCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);
    }
    @PostMapping("/customers")
    public Customer saveCustomer (Customer customer) {
        return customerService.saveCustomer(customer);
    }
}
