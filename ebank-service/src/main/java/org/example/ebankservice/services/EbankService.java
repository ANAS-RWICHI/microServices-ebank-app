package org.example.ebankservice.services;


import org.example.ebankservice.Entities.BankAccount;
import org.example.ebankservice.Repository.bankAccountRepository;
import org.example.ebankservice.feign.CustomerRestClient;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
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
    @McpTool(name = "get_all_customer_accounts",description = "Get all customer accounts from the database")
    public List<BankAccount> getAllCustomerAccounts() {
        List<BankAccount>  bankAccounts =bankAccountRepository.findAll();
        for (BankAccount bankAccount : bankAccounts) {
            bankAccount.setCustomer(
                    customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        }
        return bankAccounts;
    }
    @McpTool(name = "find_bank_account_by_id",description = "Find a bank account by its ID")
    public BankAccount findBankAccountById(@McpToolParam(description = "the ID of the bank account to find") String id) {
         BankAccount bankAccount = bankAccountRepository.findById(
                 String.valueOf(id)).orElseThrow(() -> new RuntimeException("Bank account not found")
         );
         bankAccount.setCustomer(
                 customerRestClient.getCustomerById(bankAccount.getCustomerId()));
         return bankAccount;
    }

    @McpTool(name = "save_bank_account",description = "Save a bank account to the database")
    public BankAccount saveBankAccount(@McpToolParam(description = "the bank account to save") BankAccount bankAccount) {
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        return bankAccountRepository.save(bankAccount);
    }
}
