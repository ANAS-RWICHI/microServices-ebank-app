package org.example.ebankservice.Controllers;

import org.example.ebankservice.Entities.BankAccount;
import org.example.ebankservice.services.EbankService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EbankRestController {
     private final EbankService ebankService;


     public EbankRestController(EbankService ebankService) {
         this.ebankService = ebankService;}
    @GetMapping("/accounts")
    public List<BankAccount> getAllCustomerAccounts() {
        return ebankService.getAllCustomerAccounts();
    }
    @GetMapping("/accounts/{id}")
    public BankAccount getBankAccountById(@PathVariable String id) {
        return ebankService.findBankAccountById(id);
    }

    @GetMapping("/accounts/customer/{customerId}")
    public BankAccount getCustomerAccountById( @PathVariable String customerId) {
        return ebankService.findBankAccountById(customerId);
    }
    @PostMapping("/accounts")
    public BankAccount saveBankAccount(BankAccount bankAccount) {
        return ebankService.saveBankAccount(bankAccount);
    }
}
