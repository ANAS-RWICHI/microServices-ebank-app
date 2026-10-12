package org.example.ebankservice.Repository;

import org.example.ebankservice.Entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface bankAccountRepository extends JpaRepository<BankAccount, String> {
}
