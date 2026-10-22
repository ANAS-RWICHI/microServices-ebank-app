package com._38_.customerservice;

import com._38_.customerservice.Entities.Customer;
import com._38_.customerservice.services.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerService customerService) {
        return args -> {
            List<String> names = List.of("John", "Jane", "Alice", "Bob");
            names.forEach(name -> customerService.saveCustomer(Customer.builder().name(name).email(name + "@example.com").build()));
        };
    }
}
