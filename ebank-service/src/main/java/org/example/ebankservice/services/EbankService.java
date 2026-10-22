package org.example.ebankservice.services;


import org.example.ebankservice.Entities.BankAccount;
import org.example.ebankservice.Repository.bankAccountRepository;
import org.example.ebankservice.feign.CustomerRestClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EbankService {
    private final bankAccountRepository bankAccountRepository;
    private final CustomerRestClient customerRestClient;


    public EbankService(bankAccountRepository bankAccountRepository , CustomerRestClient customerRestClient) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRestClient = customerRestClient;
    }

    public List<BankAccount> getAllCustomerAccounts() {
        List<BankAccount>  bankAccounts =bankAccountRepository.findAll();
        for (BankAccount bankAccount : bankAccounts) {
            bankAccount.setCustomer(
                    customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        }
        return bankAccounts;
    }

    public BankAccount findBankAccountById(String id) {
         BankAccount bankAccount = bankAccountRepository.findById(
                 String.valueOf(id)).orElseThrow(() -> new RuntimeException("Bank account not found")
         );
         bankAccount.setCustomer(
                 customerRestClient.getCustomerById(bankAccount.getCustomerId()));
         return bankAccount;
    }


    public BankAccount saveBankAccount(BankAccount bankAccount) {
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        return bankAccountRepository.save(bankAccount);
    }
}
