package com._38_.customerservice.services;

import com._38_.customerservice.Entities.Customer;
import com._38_.customerservice.Repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService (CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List <Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
     public Customer findCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(()->new RuntimeException("customer not found"));
     }

     public Customer saveCustomer (Customer customer) {
        return customerRepository.save(customer);
     }


}
