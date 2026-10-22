package org.example.ebankservice;

import org.example.ebankservice.Entities.BankAccount;
import org.example.ebankservice.services.EbankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class EbankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);

    }

    @Bean
    CommandLineRunner commandLineRunner(EbankService ebankService) {
        return args -> {
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 3; j++) {
                    ebankService.saveBankAccount(BankAccount.builder()
                                    .type(Math.random() > 0.5 ? "SAVINGS" : "CURRENT")
                                    .balance(Math.random() * 10000)
                                    .customerId(i)
                            .build());
                }
            }
        };

    }

}
